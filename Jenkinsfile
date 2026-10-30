pipeline {
    agent { label 'arm64' }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment') {
            steps {
                sh '''
                    java -version
                    git --version
                    test -f mvnw
                    chmod +x mvnw
                    ./mvnw -version
                '''
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw -B clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarQube-washi-srv') {
                    sh '''
                ./mvnw -B \
                  org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                  -Dsonar.projectKey=bank-payment-service
            '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build completata correttamente'
        }
        failure {
            echo 'Build fallita: controllare la console Jenkins'
        }
    }
}
