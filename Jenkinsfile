pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'JDK 11'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the WAR artifact...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Deliver') {
            steps {
                echo 'Delivery stage complete — artifact archived and ready for deployment.'
                echo "WAR file: ${env.BUILD_URL}artifact/target/"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the test results and logs above.'
        }
        always {
            cleanWs()
        }
    }
}
