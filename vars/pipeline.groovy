
def call() {
    pipeline {
        agent any
        environment {
            GITHUB_TOKEN = credentials('GITPAT')
        }
        stages {
            checkoutAndBuild()
            owaspFsScan()
            dockerBuild()
        }
    }
}

def checkoutAndBuild() {
    stage('checkout and build') {
        agent {
            docker {
                image 'maven:3.8.1-adoptopenjdk-11'
                args '-v $HOME/.m2:/root/.m2'
            }
        }
        steps {
            script {
                checkout([$class: 'GitSCM',
                           branches: [[name: '*/main']],
                           userRemoteConfigs: [[url: "https://$GITHUB_TOKEN@github.com/aman4433/spring-boot-hello-world.git"]]])
            }
            sh 'pwd'
            sh 'ls -ltr'
            sh 'mvn --version'
            sh 'mvn clean install'
        }
    }
}

def owaspFsScan() {
    stage('OWASP FS SCAN') {
        steps {
            dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'Dependency-Check'
            dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
        }
    }
}

def dockerBuild() {
    stage('docker build') {
        agent any
        steps {
            script {
                // Access Git commit ID using GIT_COMMIT environment variable
                def gitCommitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                echo "Git Commit ID: ${gitCommitId}"
                sh "docker build -t hello-world:${gitCommitId} ."
            }
        }
    }
}
