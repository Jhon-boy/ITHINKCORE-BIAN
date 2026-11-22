terraform {
  required_version = ">= 1.6"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }

  # Estado remoto: vive en rg-tfstate, que NO se destruye con la app.
  # IMPORTANTE: reemplaza el valor de storage_account_name por el nombre
  # exacto de tu cuenta de almacenamiento (el bloque backend no admite variables).
  backend "azurerm" {
    resource_group_name  = "rg-tfstate"
    storage_account_name = "sttfstatecorebianjc"
    container_name       = "tfstate"
    key                  = "core-bian.tfstate"
    use_azuread_auth     = true # autenticación con Microsoft Entra, sin claves
  }
}

# El ID de suscripción se toma de la variable de entorno ARM_SUBSCRIPTION_ID
# (el workflow la define; en tu PC la defines tú antes de ejecutar Terraform).
provider "azurerm" {
  features {
    log_analytics_workspace {
      # Evita que el workspace borrado quede 14 días reservando su nombre.
      permanently_delete_on_destroy = true
    }
  }
}
