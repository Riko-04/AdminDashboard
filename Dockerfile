FROM eclipse-temurin:21-jdk-alpine-3.21

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
# Build the application JAR file before building this Docker image
# Use the following command to build the Docker image:
# docker build -t app . 