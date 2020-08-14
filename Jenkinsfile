pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git branch: 'master', credentialsId: '01ce5bda-d477-47da-af9b-37b37a9cc812', url: 'https://github.com/martinfmi/webcrawler.git'
                bat 'mvn package'
            }
        }
        stage('Build Docker image') {
            steps {
                bat 'docker image build -t webcrawler:1.0.0 %WORKSPACE%'
            }
        }
        stage('Create Docker container') {
            steps {
                bat 'docker container create webcrawler:1.0.0'
            }
        }
        
    }
}