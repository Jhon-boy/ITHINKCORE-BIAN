locals {
  tags = {
    proyecto      = "core-bian"
    gestionado_por = "terraform"
  }
}

resource "azurerm_resource_group" "rg" {
  name     = "rg-core-bian-aca"
  location = var.location
  tags     = local.tags
}

resource "azurerm_container_registry" "acr" {
  name                = "acrcorebian${var.sufijo}"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "Basic"
  # Credenciales de administrador: la identidad de GitHub solo tiene el rol
  # Contributor, que no puede crear asignaciones de rol (AcrPull). Para
  # endurecerlo más adelante, usa identidad administrada + rol AcrPull.
  admin_enabled = true
  tags          = local.tags
}

resource "azurerm_log_analytics_workspace" "logs" {
  name                = "log-core-bian-${var.sufijo}"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "PerGB2018"
  retention_in_days   = 30
  tags                = local.tags
}

resource "azurerm_container_app_environment" "env" {
  name                       = "cae-core-bian"
  resource_group_name        = azurerm_resource_group.rg.name
  location                   = azurerm_resource_group.rg.location
  log_analytics_workspace_id = azurerm_log_analytics_workspace.logs.id
  tags                       = local.tags

  # Un entorno sano tarda pocos minutos; si se atasca, falla antes.
  timeouts {
    create = "45m"
    delete = "20m"
  }
}

resource "azurerm_container_app" "app" {
  name                         = "app-core-bian"
  container_app_environment_id = azurerm_container_app_environment.env.id
  resource_group_name          = azurerm_resource_group.rg.name
  revision_mode                = "Single"
  tags                         = local.tags

  registry {
    server               = azurerm_container_registry.acr.login_server
    username             = azurerm_container_registry.acr.admin_username
    password_secret_name = "acr-password"
  }

  secret {
    name  = "acr-password"
    value = azurerm_container_registry.acr.admin_password
  }

  ingress {
    external_enabled = true
    target_port      = var.app_port

    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }

  template {
    min_replicas = 0 # escala a cero cuando no hay tráfico
    max_replicas = 1 # una sola réplica: cada réplica tendría su propia base H2

    container {
      name   = "core-bian"
      image  = "${azurerm_container_registry.acr.login_server}/core-bian:${var.image_tag}"
      cpu    = 0.5
      memory = "1Gi"
    }
  }
}