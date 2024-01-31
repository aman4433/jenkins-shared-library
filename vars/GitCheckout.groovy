// GitCheckout.groovy

def call() {
    pipeline {
        agent any

        environment {
            GITHUB_TOKEN = credentials('GITPAT')
        }

        stages {
            stage('Git Checkout') {
                steps {
                    script {
                        checkout([$class: 'GitSCM', 
                                  branches: [[name: '*/main']], 
                                  userRemoteConfigs: [[url: "https://$GITHUB_TOKEN@github.com/aman4433/spring-boot-hello-world.git"]]])
                    }
                }
            }
        }
    }
}
