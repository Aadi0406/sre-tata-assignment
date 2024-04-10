pipeline {
    agent any

    parameters {
        string(name: 'projectId', defaultValue: '', description: 'Please provide your GCP Project ID where we need to run this pipeline')
        string(name: 'GIT_URL', defaultValue: '', description: 'git url for the above project')
        string(name: 'GIT_BRANCH', defaultValue: '', description: 'git branch for the above project')
    }
    
    environment {
        PROJECT="${params.projectId}"
        GOOGLE_APPLICATION_CREDENTIALS = credentials("${params.projectId}")
        GIT_URL="${params.GIT_URL}"
        GIT_BRANCH = "${params.GIT_BRANCH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                git clone ${GIT_URL}
                git checkout ${GIT_BRANCH}
            }
        }
        
        stage('Terraform Init') {
            steps {
                script {
                    gcloud auth activate-service-account \
                    --key-file="${GOOGLE_APPLICATION_CREDENTIALS}" \
                    --project="${PROJECT}"

                    sh 'terraform init'
                }
            }
        }
        
        stage('Terraform Apply') {
            steps {
                script {
                    sh 'terraform apply -auto-approve'
                }
            }
        }
    }
}
