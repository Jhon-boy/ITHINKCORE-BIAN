variable "sufijo" {
  description = "jh"
  type        = string

  validation {
    condition     = can(regex("^[a-z0-9]{2,8}$", var.sufijo))
    error_message = "Error en compilacion"
  }
}

variable "location" {
  description = "Región de Azure donde se crean los recursos"
  type        = string
  default     = "eastus2"
}

variable "image_tag" {
  description = "Etiqueta de la imagen Docker a desplegar"
  type        = string
  default     = "latest"
}

variable "app_port" {
  description = "Puerto en el que escucha la aplicación dentro del contenedor"
  type        = number
  default     = 8088
}
