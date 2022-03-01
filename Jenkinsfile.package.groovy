pipeline {
    agent any
    stages{
        stage('Check Condition'){
            steps{
                echo 'Nexus setting up is done'
            }
        }
        stage('Checkout Source Code'){
            steps{
                git branch: "${params.source_code_branch}", credentialsId: "${params.github_token}", url: "${params.source_code_repository_url}"
                echo "Checkout source code done ${source_code_repository_url}"
            }
        }
        stage('install packages'){
            steps{
                sh 'make clean'
                sh 'make install'
            }
        }
        stage('Python lint'){
            steps{
                sh 'make lint'
            }
        }
        stage('Test & Coverage'){
            steps{
                echo 'make test'
                echo 'make coverage'
            }
        }
        stage('build package'){
            steps{
                echo 'make dist'
            }
        }
        stage('release package'){
            steps{
                echo 'make release'
            }
        }
    }
}
