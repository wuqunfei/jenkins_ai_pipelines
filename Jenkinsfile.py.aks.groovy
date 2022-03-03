pipeline {
    agent any
    environment {
        def dockerImage
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
                script {
                    dockerImage = docker.build("${params.application_name}:${env.BUILD_ID}")
                }
                echo "docker build done"
            }
        }
        stage("Docker Publish ACR"){
            steps{
                script{
                    docker_register_url =  "https://${params.acr_name}.azurecr.io"
                    docker.withRegistry( docker_register_url, "${params.acr_credential}" ) {
                        dockerImage.push("latest")
                    }
                }
                echo "docker push done"
            }
        }
        stage("Kubernetes Deploy"){
            steps{
                withCredentials([kubeconfigContent(credentialsId: 'k8s', variable: 'KUBECONFIG_FILE')]) {
                    echo "$KUBECONFIG_FILE"
                    echo "K8s deploy is done"
                }
            }
        }
        stage("Service Health Check"){
            steps{
                echo "Service is up"
            }
        }
    }
}