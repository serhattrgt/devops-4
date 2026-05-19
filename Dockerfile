FROM openjdk:25-ea-jdk-slim

LABEL authors="yusufokr0"

WORKDIR /app

COPY build/libs/app.jar app.jar
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
