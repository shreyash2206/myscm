# Stage 1: Build the application
FROM maven:3.9-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/myscm-0.0.1-SNAPSHOT.jar /app/myscm-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/myscm-0.0.1-SNAPSHOT.jar"]
