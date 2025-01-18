# Use OpenJDK 21 as the base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the correct path in the build context
COPY /myscm-0.0.1-SNAPSHOT.jar /app/myscm-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Set the entry point for the container to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/myscm-0.0.1-SNAPSHOT.jar"]
