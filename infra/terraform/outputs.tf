output "resource_group" {
  value = azurerm_resource_group.rg.name
}

output "acr_login_server" {
  value = azurerm_container_registry.acr.login_server
}

output "app_name" {
  value = azurerm_container_app.app.name
}

output "app_url" {
  value = "https://${azurerm_container_app.app.ingress[0].fqdn}"
}
