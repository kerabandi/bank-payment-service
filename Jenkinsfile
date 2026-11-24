pipeline {
    agent { label 'k8s-java21' }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/kerabandi/bank-payment-service.git'
            }
        }

        stage('Environment') {
            steps {
                container('maven') {
                    sh '''
                        java -version
                        mvn -version
                        chmod +x mvnw
                        ./mvnw -version
                    '''
                }
            }
        }

        stage('Build & Test') {
            steps {
                container('maven') {
                    sh '''
                        ./mvnw -B \
                          -Dmaven.repo.local="$WORKSPACE/.m2/repository" \
                          clean verify
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build Quarkus su K3s completata'
        }

        failure {
            echo 'Build fallita: verificare la console Jenkins'
        }
    }
}
