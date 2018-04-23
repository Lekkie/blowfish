FROM centos:latest
FROM java:8
MAINTAINER Lekan Omotayo <lekkie.aydot@gmail.com>

ADD VERSION .

ARG BLOWFISH_VERSION=1.0-SNAPSHOT
# Update CentOS 7
#RUN yum update -y && yum upgrade -y
# Install packages
#RUN yum install -y unzip wget curl git
# Install EPEL Repository
#RUN yum install -y epel-release
# Clean CentOS 7
#RUN yum clean all

EXPOSE 8080

ENV DB_NAME blowfish
ENV DB_USER blowfish
ENV DB_PASSWD nk30ny3
ENV DB_URL jdbc:mysql://10.0.0.28:3306/blowfish
ENV DB_DRIVER com.mysql.cj.jdbc.Driver
ENV DB_PLATFORM mysql
ENV DB_DATABASE_PLATFORM org.hibernate.dialect.MySQLDialect
#ENV DB_URL jdbc:h2:mem:blowfish;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
#ENV DB_DRIVER org.h2.Driver
#ENV DB_PLATFORM h2
#ENV DB_DATABASE_PLATFORM org.hibernate.dialect.H2Dialect

ENV H2_ENABLED false

ENV REDIS_CLUSTER_MODE false
ENV REDIS_HOST redis1.4un0o7.ng.0001.euw2.cache.amazonaws.com
ENV REDIS_PORT 6379
ENV REDIS_CLUSTER_NODES redis1.4un0o7.ng.0001.euw2.cache.amazonaws.com:6379,redis1-002.4un0o7.0001.euw2.cache.amazonaws.com:6379,redis1-003.4un0o7.0001.euw2.cache.amazonaws.com:6379
ENV REDIS_CLUSTER_MAX_REDIRECT 3

VOLUME /tmp
#WORKDIR /root/blowfish

ADD application.yml /root/blowfish/application.yml
ADD blowfish-1.0-SNAPSHOT.jar /root/blowfish/blowfish.jar
RUN bash -c 'touch /root/blowfish/blowfish.jar'
ADD install.sh /root/blowfish/install.sh
RUN bash -c '/root/blowfish/install.sh'

# lets default to the blowfish dir so folks can more easily navigate to around the server install
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/root/blowfish/blowfish.jar", "--spring.config.location=/root/blowfish/application.yml"]

#CMD ["java","-jar", "-Dspring.config.location=/target/application.properties", "/home/blowfish-1.0-SNAPSHOT.jar"]


