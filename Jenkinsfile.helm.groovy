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
                sh "helm package ${params.helm_package_folder}"
            }
        }
        stage('helm push') {
//             when { tag "release-*" }
            environment {
                tag = gitTagName()
            }
            steps {
                echo "helm is pushing to helm repository: ${parames.helm_register_sever}"
                //sh "helm push ${params.helm_package_folder}/$GIT_COMMIT.take(7)/tgz.oci oci://${parames.helm_repository} --kubeconfig ${KUBE_CONFIG_PATH}"
                echo "helm pushed new version ${tag}"
            }
        }
    }
}

String gitTagName() {
    commit = getCommit()
    if (commit) {
        desc = sh(script: "git describe --tags ${commit}", returnStdout: true)?.trim()
        if (isTag(desc)) {
            return desc
        }
    }
    return null
}

String getCommit() {
    return sh(script: 'git rev-parse HEAD', returnStdout: true)?.trim()
}

@NonCPS
boolean isTag(String desc) {
    match = desc =~ /.+-[0-9]+-g[0-9A-Fa-f]{6,}$/
    result = !match
    match = null // prevent serialisation
    return result
}
