#IMAGE: Get the base image for Liberty
FROM websphere-liberty:kernel

# Add MySQL  Type 4 JDBC driver
RUN mkdir /opt/ibm/wlp/usr/shared/resources/mysql
COPY wlp/usr/shared/resources/mysql/mysql-connector-java-5.1.38.jar /opt/ibm/wlp/usr/shared/resources/mysql/
USER root
RUN chown 1001:0 /opt/ibm/wlp/usr/shared/resources/mysql/*.jar
USER 1001

# Add Hazelcast config
RUN mkdir opt/ibm/wlp/usr/shared/config/hazelcast
COPY wlp/usr/shared/config/hazelcast/*.xml /opt/ibm/wlp/usr/shared/config/hazelcast/
USER root
RUN chown 1001:0 /opt/ibm/wlp/usr/shared/config/hazelcast/*.xml
USER 1001

# Add Hazelcast jar
RUN mkdir /opt/ibm/wlp/usr/shared/resources/hazelcast
COPY wlp/usr/shared/resources/hazelcast/*.jar /opt/ibm/wlp/usr/shared/resources/hazelcast/
USER root
RUN chown 1001:0 /opt/ibm/wlp/usr/shared/resources/hazelcast/*.jar
USER 1001

#BINARIES: Add in all necessary application binaries
COPY wlp/config/server.xml /config
USER root
RUN chown 1001:0 /config/server.xml
USER 1001
ENV JAVA_TOOL_OPTIONS="-Dhazelcast.jcache.provider.type=server ${JAVA_TOOL_OPTIONS}"

RUN configure.sh


ADD target/plants-by-websphere-jee6-mysql.ear /opt/ibm/wlp/usr/servers/defaultServer/apps
