pipeline {
    environment {
        KEYVAULT = 'ai-devops-azverpjq'
        KUBE_CONFIG_PATH = './kubeconfig'
        HELM_EXPERIMENTAL_OCI = 1
    }
    agent any
    stages {
        stage('Checkout Helm Code') {
            steps {
                git branch: "${params.source_code_branch}", credentialsId: "${params.github_token_credential}", url: "${params.source_code_repository_url}"
                echo "Checkout helm code done ${source_code_repository_url}"
            }
        }
        stage('helm config and login by cloud') {
            steps {
                sh "helm version"
                withCredentials([string(credentialsId: 'AZURE_CLIENT_ID', variable: 'AZURE_CLIENT_ID'),
                                 string(credentialsId: 'AZURE_CLIENT_SECRET', variable: 'AZURE_CLIENT_SECRET'),
                                 string(credentialsId: 'AZURE_ARM_TENANT_ID', variable: 'AZURE_ARM_TENANT_ID'),
                                 string(credentialsId: 'AZURE_SUBSCRIPTION_ID', variable: 'AZURE_SUBSCRIPTION_ID')]) {

                    sh "az login --service-principal --user $AZURE_CLIENT_ID --password $AZURE_CLIENT_SECRET --tenant $AZURE_ARM_TENANT_ID"
                    sh "az account set --subscription $AZURE_SUBSCRIPTION_ID"
                    sh "rm -fr ${KUBE_CONFIG_PATH}"
                    sh "az keyvault secret download --vault-name $KEYVAULT --name aks-cicd-kubeconfig --file ${KUBE_CONFIG_PATH}"
                    sh "export ACR_NAME=\$(az keyvault secret show --vault-name $KEYVAULT --name customer-tenant-acr-name)"
                }
            }
        }
        stage('helm package') {
            steps {
                sh "heml lint ${params.helm_package_folder}"
                sh "helm package ${params.helm_package_folder} --version ${GIT_COMMIT.take(7)} --kubeconfig ${KUBE_CONFIG_PATH}"
            }
        }
        stage('helm push') {
            steps {
                echo "helm is pushing version:${GIT_COMMIT.take(7)} @ helm repository: ${parames.helm_repository}"
                sh "helm push ${params.helm_package_folder}/$GIT_COMMIT.take(7)/tgz.oci oci://${parames.helm_repository} --kubeconfig ${KUBE_CONFIG_PATH}"
                echo "helm pushed new version ${GIT_COMMIT.take(7)}"
            }
        }
    }
}
