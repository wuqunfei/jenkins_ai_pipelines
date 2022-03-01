pipeline {
    agent any
    stages{
        stage('Preconditions'){
            steps{
                echo 'Nexus setting up is done'
            }
        }
        stage('Checkout'){
            steps{
                echo 'Checkout source code done'
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
