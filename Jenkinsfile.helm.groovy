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
            }
        }
        stage('helm package') {
            steps {
                sh "helm lint ${params.helm_package_folder}"
                sh "helm package ${params.helm_package_folder} --version ${GIT_TAG}"
            }
        }
        stage('helm push') {
            steps {
                echo "helm is pushing version:${GIT_TAG} @ helm repository: ${parames.helm_repository}"
                //sh "helm push ${params.helm_package_folder}/$GIT_COMMIT.take(7)/tgz.oci oci://${parames.helm_repository} --kubeconfig ${KUBE_CONFIG_PATH}"
                echo "helm pushed new version ${GIT_TAG}"
            }
        }
    }
}
