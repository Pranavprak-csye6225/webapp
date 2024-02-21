packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

source "googlecompute" "centos8_webapp" {
  project_id            = var.project_id
  source_image_family   = var.source_image_family
  zone                  = var.zone
  ssh_username          = var.ssh_username
  network               = var.network
  image_name            = var.image_name
  image_description     = var.image_description
  service_account_email = var.service_account_email
}

build {
  sources = [
    "sources.googlecompute.centos8_webapp"
  ]
  provisioner "shell" {
    script = "./script/create-nologin-user.sh"
  }
  provisioner "file" {
    source      = "./webapp.service"
    destination = "/tmp/"
  }


  provisioner "shell" {
    script = "./script/install-java.sh"
  }
  provisioner "shell" {
    script = "./script/install-mysql.sh"
  }

  provisioner "file" {
    source      = "../target/webapp-1.1.0.jar"
    destination = "/tmp/"
  }
  provisioner "file" {
    source      = "../.env"
    destination = "/tmp/"
  }
  provisioner "shell" {
    script = "./script/transfer-ownership.sh"
  }
  provisioner "shell" {
    script = "./script/startup-service.sh"
  }

}
