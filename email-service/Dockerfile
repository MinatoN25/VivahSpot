FROM openjdk:11
ADD ./target/email-service-0.0.1-SNAPSHOT.jar /usr/src/email-service-0.0.1-SNAPSHOT.jar
ADD ./invoice.jrxml /usr/src/invoice.jrxml
ADD ./invoice.jasper /usr/src/invoice.jasper
WORKDIR usr/src
ENTRYPOINT ["java","-jar", "email-service-0.0.1-SNAPSHOT.jar"]
