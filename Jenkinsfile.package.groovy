pipeline {
    agent any
    stages {
        stage('Check Condition') {
            steps {
                echo 'Nexus setting up is done'
            }
        }
        stage('Checkout Source Code') {
            steps {
                git branch: "${params.source_code_branch}", credentialsId: "${params.github_token}", url: "${params.source_code_repository_url}"
                echo "Checkout source code done ${source_code_repository_url}"
            }
        }
        stage('install packages') {
            steps {
                sh 'make clean'
                sh 'make install'
            }
        }
        stage('Python lint') {
            steps {
                sh 'make lint'
            }
        }
        stage('Test & Coverage') {
            steps {
                sh 'make test'
                sh 'make coverage'
            }
        }
        stage('build package') {
            steps {
                sh 'make dist'
            }
        }
//        Setting nexus information into environment, twine can upload artifacts
        stage('release package') {
            environment {
                TWINE_REPOSITORY_URL = "${params.nexus_py_repository_url}"
            }
            steps {
                withCredentials([usernamePassword(credentialsId: "${params.nexus_credential}", usernameVariable: 'TWINE_USERNAME', passwordVariable: 'TWINE_PASSWORD')]) {
                    echo "${TWINE_REPOSITORY_URL}"
                    echo "${TWINE_USERNAME}"
                    sh 'make release'
                }
            }
        }
    }
}
