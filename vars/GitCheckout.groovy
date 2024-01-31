// GitCheckout.groovy

def call(String repoUrl, String branch) {
    pipeline {
        agent any

        stages {
            stage('Git Checkout') {
                steps {
                    script {
                        checkout([$class: 'GitSCM', 
                                  branches: [[name: branch]], 
                                  userRemoteConfigs: [[url: repoUrl]]])
                    }
                }
            }
        }
    }
}
