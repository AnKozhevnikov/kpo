FROM openjdk:17-jdk-slim
COPY target/gateway-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
