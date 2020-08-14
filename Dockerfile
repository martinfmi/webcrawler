# use AdoptOpenJDK build of JDK 13​
FROM adoptopenjdk/openjdk13:centos

RUN mkdir /opt/app​

WORKDIR /opt/app​

COPY target/webcrawler.spring.boot-0.0.1-SNAPSHOT.jar /opt/app​

CMD ["java", "-jar", "webcrawler.spring.boot-0.0.1-SNAPSHOT.jar"]
