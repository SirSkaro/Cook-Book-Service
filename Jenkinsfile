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
                withMaven {
                	sh label: '', script: './mvnw clean package -DskipTests'
               	}
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker build -t cookbook-service .'
                sh 'docker stop cookbook-service || true && docker rm cookbook-service || true'
                sh 'docker run -d -p 8080:80 --network casa-net --name cookbook-service --restart always'
            }
        }
    }
}


        