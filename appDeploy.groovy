pipeline {
    agent any
    
    environment {
        GIT_URL='https://github.com/Aadi0406/sre-tata-assignment.git'
        GIT_BRANCH = 'master'
        GKE_CLUSTER = 'gke-cluster'
        GKE_ZONE = 'asia-south1'
        GKE_PROJECT = 'august-monolith-418704'
        GKE_NAMESPACE = 'default'
        DOCKER_REGISTRY = 'asia-south1-docker.pkg.dev/august-monolith-418704'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git clone ${GIT_URL}
                git checkout ${GIT_BRANCH}
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/app-image/hello-world-image:latest")
                }
            }
        }
        
        stage('Push Docker Image to GAR') {
            steps {
                script {
                    docker.withRegistry('', 'gcp-service-account-credentials') {
                        docker.image("${DOCKER_REGISTRY}/app-image/hello-world-image:latest").push()
                    }
                }
            }
        }
        
        stage('Deploy to GKE') {
            steps {
                script {
                    sh "gcloud container clusters get-credentials ${GKE_CLUSTER} --zone ${GKE_ZONE} --project ${GKE_PROJECT}"
                    sh "kubectl apply -f deployment.yaml -n ${GKE_NAMESPACE}"
                    sh "kubectl apply -f service.yaml -n ${GKE_NAMESPACE}"
                }
            }
        }
    }
}
