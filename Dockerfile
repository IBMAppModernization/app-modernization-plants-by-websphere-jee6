#IMAGE: Get the base image for Liberty
FROM websphere-liberty:kernel

# Add MariaDB Type 4 JDBC driver
#RUN mkdir /opt/ibm/wlp/usr/shared/resources/mariadb
#COPY docker/liberty/mariadb-java-client-1.7.4.jar /opt/ibm/wlp/usr/shared/resources/mariadb/

# Add MySQL  Type 4 JDBC driver
RUN mkdir /opt/ibm/wlp/usr/shared/resources/mysql
USER root
COPY  wlp/usr/shared/resources/mysql/mysql-connector-java-5.1.38.jar /opt/ibm/wlp/usr/shared/resources/mysql/
RUN chown 1001:0  /opt/ibm/wlp/usr/shared/resources/mysql/mysql-connector-java-5.1.38.jar

#BINARIES: Add in all necessary application binaries
COPY  wlp/config/server.xml /config
RUN chown 1001:0 /config/server.xml

ADD target/plants-by-websphere-jee6-mysql.ear /opt/ibm/wlp/usr/servers/defaultServer/apps
RUN chown 1001:0 /opt/ibm/wlp/usr/servers/defaultServer/apps/plants-by-websphere-jee6-mysql.ear

### Hazelcast Session Caching ###
# Copy the Hazelcast libraries from the Hazelcast Docker image
COPY --from=hazelcast/hazelcast /opt/hazelcast/lib/*.jar /opt/ibm/wlp/usr/shared/resources/hazelcast/
RUN chown 1001:0 /opt/ibm/wlp/usr/shared/resources/hazelcast
RUN chown 1001:0 /opt/ibm/wlp/usr/shared/resources/hazelcast/*.jar
USER 1001

# Instruct configure.sh to copy the client topology hazelcast.xml
# ARG HZ_SESSION_CACHE=client

# Instruct configure.sh to copy the embedded topology hazelcast.xml and set the required system property
ARG HZ_SESSION_CACHE=embedded
ENV JAVA_TOOL_OPTIONS="-Dhazelcast.jcache.provider.type=server ${JAVA_TOOL_OPTIONS}"

## This script will add the requested XML snippets and grow image to be fit-for-purpose
RUN configure.sh
