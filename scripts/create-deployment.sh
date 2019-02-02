echo "Enter your ICP namespace: "
read -r namespace

cat <<EOF > k8s/pbw-mysql-deployment.yaml
---
apiVersion: v1
kind: Service
metadata:
  name: pbw-liberty
  labels:
    app: pbw-mysql
spec:
  type: NodePort
  ports:
    - port: 9080
      protocol: TCP
      targetPort: 9080

  selector:
    app: pbw-mysql
    tier: web-app
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: pbw-liberty
  labels:
    app: pbw-mysql
spec:
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: pbw-mysql
        tier: web-app
    spec:
      containers:
        - image: mycluster.icp:8500/$namespace/pbw-mysql:1.0
          imagePullPolicy: Always
          name: pbw-liberty
          env:
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: pbw-mysql-credentials
                  key: username
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: pbw-mysql-credentials
                  key: password
            - name: DB_HOST
              valueFrom:
                secretKeyRef:
                  name: pbw-mysql-credentials
                  key: host
            - name: DB_PORT
              valueFrom:
                secretKeyRef:
                  name: pbw-mysql-credentials
                  key: port
          ports:
            - containerPort: 9080
              name: web
EOF
