pipeline{
    agent any
    tools{
        maven "maven"

    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/tomas-manriquez/tingeso-2024-2']])
                dir("demo"){
                    sh "mvn clean install"

            }
        }
        stage("Test"){
            steps{
                dir("demo"){
                    sh "mvn test"
                }
            }
        }
        stage("Build and Push Docker Image"){
            steps{
                dir("demo"){
                    script{
                        withDockerRegistry(credentialsId: 'docker-credentials')
                        {
                            bat "docker build -t tomasmanriquez480/backend-image:latest ."
                            bat "docker push tomasmanriquez480/backend-image:latest"
                        }
                    }
                }
            }
        }
    }
}