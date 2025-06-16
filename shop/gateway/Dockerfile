FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:go-offline
COPY src /app/src
RUN mvn clean install -DskipTests
RUN cp /app/target/gateway-1.0.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]