# TEMPORAL: Azure terminó de crear el entorno después de que Terraform dejó de
# esperar, así que existe en Azure pero no en el estado de Terraform.
# Este bloque hace que Terraform lo adopte en la próxima ejecución.
#
# BORRA ESTE ARCHIVO después de una ejecución exitosa. Si lo dejas y más
# adelante destruyes y recreas la infraestructura, el import fallaría porque
# el entorno ya no existiría.
import {
  to = azurerm_container_app_environment.env
  id = "/subscriptions/f6e05916-803f-4ea7-91a9-7f4435ad8732/resourceGroups/rg-core-bian-aca/providers/Microsoft.App/managedEnvironments/cae-core-bian"
}
