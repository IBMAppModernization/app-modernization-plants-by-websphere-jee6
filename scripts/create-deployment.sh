echo "Enter your ICP namespace: "
read -r namespace

cat <<EOF > k8s/pbw-liberty-mariadb-deployment.yaml
---
apiVersion: v1
kind: Service
metadata:
  name: pbw-liberty
  labels:
    app: pbw-liberty-mariadb
spec:
  type: NodePort
  ports:
    - port: 9080
      protocol: TCP
      targetPort: 9080

  selector:
    app: pbw-liberty-mariadb
    tier: web-app
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: pbw-liberty
  labels:
    app: pbw-liberty-mariadb
spec:
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: pbw-liberty-mariadb
        tier: web-app
    spec:
      containers:
        - image: mycluster.icp:8500/$namespace/pbw-mariadb-web:1.0.0
          imagePullPolicy: Always
          name: pbw-liberty
          env:
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: pbw-liberty-mariadb-credentials
                  key: username
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: pbw-liberty-mariadb-credentials
                  key: password
            - name: DB_HOST
              valueFrom:
                secretKeyRef:
                  name: pbw-liberty-mariadb-credentials
                  key: host
            - name: DB_PORT
              valueFrom:
                secretKeyRef:
                  name: pbw-liberty-mariadb-credentials
                  key: port
          ports:
            - containerPort: 9080
              name: web
---
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  annotations:
    ingress.kubernetes.io/rewrite-target: /    # This line is only needed when deploying on IBM Cloud Private.
  name: pbw-$namespace-ingress  # The ingress name must be unique for each ingress rule created.
  labels:
    app:  pbw-liberty-mariadb

spec:
  rules:
  - host:
    http:
      paths:
      - path: /pbw-$namespace-ui  # This is the url path used to access the service externally.
        backend:
          serviceName: pbw-liberty # This is the name of the service to be exposed.
          servicePort: 9080

EOF
