#IMAGE: Get the base image for Liberty
FROM websphere-liberty:webProfile7

# Add MariaDB Type 4 JDBC driver
#RUN mkdir /opt/ibm/wlp/usr/shared/resources/mariadb
#COPY docker/liberty/mariadb-java-client-1.7.4.jar /opt/ibm/wlp/usr/shared/resources/mariadb/

# Add MySQL  Type 4 JDBC driver
RUN mkdir /opt/ibm/wlp/usr/shared/resources/mysql
COPY docker/liberty/mysql-connector-java-5.1.38.jar /opt/ibm/wlp/usr/shared/resources/mysql/


# Install all required Liberty modules
RUN /opt/ibm/wlp/bin/installUtility install --verbose  --acceptLicense \
	jsp-2.3 \
	servlet-3.1 \
  ejbLite-3.2 \
  ejbRemote-3.2 \
  jsf-2.2 \
  beanValidation-1.1 \
	jndi-1.0 \
	jdbc-4.2 \
  cdi-1.2 \
	javaMail-1.5 \
  el-3.0 \
	jpa-2.1

#BINARIES: Add in all necessary application binaries
COPY wlp/server.xml /config
ADD pbw-ear/target/plants-by-websphere-jee6-mysql.ear /opt/ibm/wlp/usr/servers/defaultServer/apps
