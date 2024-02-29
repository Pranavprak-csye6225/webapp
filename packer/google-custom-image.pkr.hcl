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
  image_name            = "${var.image_name}-{{timestamp}}"
  image_description     = var.image_description
  service_account_email = var.service_account_email
}

build {
  sources = [
    "sources.googlecompute.centos8_webapp"
  ]
  provisioner "file" {
    source      = "./webapp.service"
    destination = "/tmp/"
  }
  provisioner "file" {
    source      = "./webapp.path"
    destination = "/tmp/"
  }
  provisioner "file" {
    source      = "../target/webapp-1.1.0.jar"
    destination = "/tmp/"
  }
  provisioner "shell" {
    scripts = ["./script/create-nologin-user.sh", "./script/install-java.sh",
    "./script/transfer-ownership.sh", "./script/startup-service.sh"]
  }



}
