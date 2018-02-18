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
ENV DB_PASSWD 51lv3r%H)#846


# ENV DB_URL jdbc:mysql://172.31.1.223:4000/blowfish
# ENV DB_DRIVER com.mysql.cj.jdbc.Driver
# ENV DB_PLATFORM mysql
# ENV DB_DATABASE_PLATFORM org.hibernate.dialect.MySQLDialect


ENV DB_URL jdbc:h2:mem:blowfish;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
ENV DB_DRIVER org.h2.Driver
ENV DB_PLATFORM h2
ENV DB_DATABASE_PLATFORM org.hibernate.dialect.H2Dialect

ENV AVERAGE_RATE_CRON 0 0 0 1/1 * ?
ENV DELETE_OLD_HISTORY_CRON 0 0 0 1/1 * ?
ENV TRADE_DELAY 21000
ENV COMPLETE_OPEN_ORDER_DELAY 300000

ENV H2_ENABLED false

VOLUME /tmp
#WORKDIR /root/blowfish

ADD application.yml /root/blowfish/application.yml
ADD blowfish-1.0-SNAPSHOT.jar /root/blowfish/blowfish.jar
RUN bash -c 'touch /root/blowfish/blowfish.jar'
ADD install.sh /root/blowfish/install.sh
RUN bash -c '/root/blowfish/install.sh'

# lets default to the blowfish dir so folks can more easily navigate to around the server install
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/root/blowfish/blowfish.jar", "--spring.config.location=/root/blowfish/application.yml"]


#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "-Dspring.config.location=/root/blowfish/application.yml", "/root/blowfish/blowfish.jar"]
#CMD ["java","-jar", "-Dspring.config.location=/target/application.properties", "/home/blowfish-1.0-SNAPSHOT.jar"]


