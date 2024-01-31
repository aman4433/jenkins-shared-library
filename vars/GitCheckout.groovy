// GitCheckout.groovy

def call(String repoUrl, String branch) {
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
                                  branches: [[name: branch]], 
                                  userRemoteConfigs: [[url: "https://$GITHUB_TOKEN@$repoUrl"]]])
                    }
                }
            }
        }
    }
}
