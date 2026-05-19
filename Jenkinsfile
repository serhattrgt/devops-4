pipeline {
    agent any

    environment {
        IMAGE_NAME = "serhat0/school-management"
        IMAGE_TAG = "latest"
    }

    stages {

        stage('Clone') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/serhattrgt/devops-4.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'gradlew.bat clean build -x test'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('DockerHub Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                }
            }
        }

        stage('Push Image') {
            steps {
                bat "docker push %IMAGE_NAME%:%IMAGE_TAG%"
            }
        }

        stage('Deploy to K8s') {
            steps {
            withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]){
                 bat 'kubectl apply --validate=false -f k8s/backend-depl.yaml'
                 bat 'kubectl apply --validate=false -f k8s/backend-service.yaml'
                 bat 'kubectl apply --validate=false -f k8s/db-depl.yaml'
                 bat 'kubectl apply --validate=false -f k8s/db-service.yaml'
                 }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}