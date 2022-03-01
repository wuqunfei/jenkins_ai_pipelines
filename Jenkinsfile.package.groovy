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
                echo 'Checkout source code done ${source_code_repository_url}'
            }
        }
        stage('Lint'){
            steps{
                echo 'Lint done'
            }
        }
        stage('Test'){
            steps{
                echo 'Test done'
            }
        }
        stage('build'){
            steps{
                echo 'Test done'
            }
        }
        stage('release'){
            steps{
                echo 'Release done'
            }
        }
    }
}
