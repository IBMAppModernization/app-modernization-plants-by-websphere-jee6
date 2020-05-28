pipeline {
  

    tools {
        maven 'Apache Maven 3.0.5'
        jdk 'Open JDK 8'
    }
    
    agent any
    stages {
     stage('Build application ear file') {
          steps {
              checkout scm
              sh 'mvn clean package'
          }
        }
    }
}
