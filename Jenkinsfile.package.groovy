pipeline {
    agent any
    stages{
        stage('Condition'){
            steps{
                echo 'Nexus setting up is done'
            }
        }
        stage('Checkout'){
            steps{
                git branch: "${params.source_code_branch}", credentialsId: "${params.github_token}", url: "${params.source_code_repository_url}"
                echo "Checkout source code done ${source_code_repository_url}"
            }
        }
        stage('install'){
            steps{
                sh 'make clean'
                sh 'make install'
            }
        }
        stage('lint'){
            steps{
                sh 'make lint'
            }
        }
        stage('Test'){
            steps{
                echo 'make test'
                echo 'make coverage'
            }
        }
        stage('build'){
            steps{
                echo 'make dist'
            }
        }
        stage('release'){
            steps{
                echo 'make release'
            }
        }
    }
}
