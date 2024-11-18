FROM openjdk:17-jdk-slim

LABEL org.opencontainers.image.authors="arshad"

RUN apt-get update \
    && apt-get install -y curl \
    && apt-get clean

COPY target/url-shortening-service-1.1-SNAPSHOT.jar url-shortening-service.jar

EXPOSE 8090
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "url-shortening-service.jar"]