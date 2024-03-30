terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.51.0"
    }
  }
}

provider "google" {
  project = "august-monolith-418704"
  region  = "asia-south1"
}

resource "google_compute_network" "vpc_network" {
  name                    = "app-vpc"
  auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "subnet" {
  name          = "subnet-asia-south1"
  region        = "asia-south1"
  network       = google_compute_network.vpc_network.self_link
  ip_cidr_range = "10.10.1.0/24"
}

resource "google_compute_firewall" "allow_http" {
  name    = "allow-http"
  network = google_compute_network.vpc_network.self_link

  allow {
    protocol = "tcp"
    ports    = ["80", "3000"]
  }

  source_ranges = ["0.0.0.0/0"]
}

resource "google_compute_firewall" "allow_ssh" {
  name    = "allow-ssh"
  network = google_compute_network.vpc_network.self_link

  allow {
    protocol = "tcp"
    ports    = ["22"]
  }

  source_ranges = ["0.0.0.0/0"]
}

resource "google_container_cluster" "gke_cluster" {
  name     = "gke-cluster"
  location = "asia-south1-a"

  remove_default_node_pool = true

  network    = google_compute_network.vpc_network.self_link
  subnetwork = google_compute_subnetwork.subnet.self_link

  node_pool {
    name = "default-pool"
    node_count = 1

    node_config {
      oauth_scopes = [
        "https://www.googleapis.com/auth/compute"
      ]

      # Adjust machine type and image type as needed
      machine_type = "e2-medium"

      labels = {
        environment = "development"
      }
    }
  }
}
