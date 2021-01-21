pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/SirSkaro/Cook-Book-Service.git'
            }
        }
        stage('Package') {
            steps {
                configFileProvider([configFile(fileId: '6ad22f3f-4a44-48e4-abd1-10e1d6a84074', targetLocation: 'src/main/resources/application.properties')]) {}
                sh label: '', script: './mvnw clean package -DskipTests'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker build -t cookbook-service .'
                sh 'docker stop cookbook-service || true && docker rm cookbook-service || true'
                withCredentials([string(credentialsId: 'e6484059-0aa9-45ca-a270-e8d2ae719ecc', variable: 'serverName')]) {
	                sh 'docker run -d \
	                	--expose 80 \
	                	--network casa-net \
	                	--name cookbook-service \
	                	--restart always \
	                	-e VIRTUAL_HOST=api.$serverName \
	                	cookbook-service'
                }
            }
        }
    }
}


        