// Pod Template
def cloud = env.CLOUD ?: "kubernetes"
def serviceAccount = env.SERVICE_ACCOUNT ?: "jnkns-for-cicd-lab-jenkins"
def registryCredsID = env.REGISTRY_CREDENTIALS ?: "registry-credentials"
def namespace = env.NAMESPACE ?: "default"
def registry = env.REGISTRY ?: "mycluster.icp:8500"
def majorPrefix = env.MAJOR_PREFIX ?: "1.0.0"

// In this multiuser scenario  we derive user specific vars from the logged in Jenkins user
//def userName = currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserId()
def userName = env.RUN_AS ?: "anonymous"
def releaseName = "pbw-liberty-mariadb-" + userName

// This assumes the last 2 digits of the username are unique (eg user04, user05 etc)
def userNumber = userName[-2..-1]
def deploymentNS = "devnamespace" + userNumber

def podLabel = "agent-" + releaseName


podTemplate(label: podLabel, cloud: cloud, serviceAccount: serviceAccount, namespace: namespace, envVars: [
        envVar(key: 'NAMESPACE', value: namespace),
        envVar(key: 'DEPLOYMENT_NS', value: deploymentNS),
        envVar(key: 'REGISTRY', value: registry),
        envVar(key: 'RELEASE_NAME', value: releaseName),
        envVar(key: 'MAJOR_PREFIX', value: majorPrefix)
    ],
    volumes: [
        hostPathVolume(hostPath: '/etc/docker/certs.d', mountPath: '/etc/docker/certs.d'),
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
    ],
    containers: [
        containerTemplate(name: 'maven', image: 'maven:3-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker' , image: 'docker:17.06.1-ce', ttyEnabled: true, command: 'cat')
  ]) {

    node(podLabel) {
        checkout scm
        container('maven') {
            stage('Build application ear file') {
               sh """
               #!/bin/bash
               mvn -B -DskipTests clean package
               """
            }
        }
        container('docker') {
            stage('Build Docker Image') {
                sh """
                #!/bin/bash
                docker build -t ${env.REGISTRY}/${env.DEPLOYMENT_NS}/pbw-mariadb-web:${env.MAJOR_PREFIX}.${env.BUILD_NUMBER} .
                """
            }
            stage('Push Docker Image to Registry') {
               withCredentials([usernamePassword(credentialsId: registryCredsID,
                                               usernameVariable: 'USERNAME',
                                               passwordVariable: 'PASSWORD')]) {
                   sh """
                   #!/bin/bash
                   docker login -u ${USERNAME} -p ${PASSWORD} ${env.REGISTRY}
                   docker push ${env.REGISTRY}/${env.DEPLOYMENT_NS}/pbw-mariadb-web:${env.MAJOR_PREFIX}.${env.BUILD_NUMBER}
                   """
               }
            }
        }
        container('kubectl') {
            stage('Deploy new Docker Image') {
                sh """
                #!/bin/bash
                DEPLOYMENT=`kubectl --namespace=${env.DEPLOYMENT_NS} get deployments -l app=pbw-liberty-mariadb,component=web-app,release=${env.RELEASE_NAME} --no-headers  -o name`
                kubectl --namespace=${env.DEPLOYMENT_NS} get \${DEPLOYMENT} --no-headers -o custom-columns=":metadata.name"
                if [ \${?} -ne "0" ]; then
                    # No deployment to update
                    echo 'No deployment to update'
                    exit 1
                fi
                # Update Deployment
                kubectl --namespace=${env.DEPLOYMENT_NS} set image \${DEPLOYMENT} ${env.RELEASE_NAME}-liberty=${env.REGISTRY}/${env.DEPLOYMENT_NS}/pbw-mariadb-web:${env.MAJOR_PREFIX}.${env.BUILD_NUMBER}
                kubectl --namespace=${env.DEPLOYMENT_NS} rollout status \${DEPLOYMENT}
                """
            }
        }
    }
}
