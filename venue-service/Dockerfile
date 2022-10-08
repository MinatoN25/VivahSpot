FROM openjdk:11

ADD ./target/venue-service-0.0.1-SNAPSHOT.jar /usr/src/venue-service-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "venue-service-0.0.1-SNAPSHOT.jar"]