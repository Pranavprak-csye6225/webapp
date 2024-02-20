packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

source "googlecompute" "centos8_webapp" {
  project_id            = "cloud-course-csye6225-dev"
  source_image_family   = "centos-stream-8"
  zone                  = "us-east4-a"
  ssh_username          = "packer"
  network               = "default"
  image_name            = "centos8-webapp"
  image_description     = "Custom image to load webapp"
  credentials_file      = "/Users/pranavprakash/Downloads/cloud-course-csye6225-dev-cffeb10ed9b3.json"
  service_account_email = "compute-access@cloud-course-csye6225-dev.iam.gserviceaccount.com"
}

build {
  sources = [
    "sources.googlecompute.centos8_webapp"
  ]
  provisioner "shell" {
    script = "script/create-nologin-user.sh"
  }

  provisioner "shell" {
    script = "script/install-java-maven.sh"
  }
  provisioner "shell" {
    script = "script/install-mysql.sh"
  }

  provisioner "file" {
    source      = "../target/webapp-1.1.0.jar"
    destination = "/tmp/"
  }
  provisioner "shell" {
    script = "script/transfer-ownership.sh"
  }

}
