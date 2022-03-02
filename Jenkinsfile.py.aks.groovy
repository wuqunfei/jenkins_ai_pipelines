pipeline {
    agent any
    environment {

    }
    stages {
        stage('Checkout Source Code and Deployment Code') {
            steps {

                git branch: "${params.deploy_code_branch}", credentialsId: "${params.github_token}", url: "${params.deploy_code_repository_url}"
                echo "Checkout source code done ${source_code_repository_url}"

                git branch: "${params.source_code_branch}", credentialsId: "${params.github_token}", url: "${params.source_code_repository_url}"
                echo "Checkout source code done ${source_code_repository_url}"


            }
        }
        stage("Test Code"){
            steps{
                echo "Test code"
            }
        }
        stage("Build Code"){
            steps{
                echo "application build done"
            }
        }
        stage("Docker Build"){
            steps{
                echo "docker build done "
            }
        }
        stage("Docker Publish ACR"){
            steps{
                echo "docker push done"
            }
        }
        stage("Kubernetes Deploy"){
            steps{
                echo "K8S deploy done"
            }
        }
        stage("Service Health Check"){
            steps{
                echo "Service is up"
            }
        }
    }
}