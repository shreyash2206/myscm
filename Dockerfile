# Stage 1: Build the application
FROM amazoncorretto:17 AS build

# Install necessary tools and Maven (specific version)
ARG MAVEN_VERSION=3.8.8
RUN yum update -y && \
    yum install -y tar gzip curl && \
    curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o /tmp/maven.tar.gz && \
    tar -xzf /tmp/maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm /tmp/maven.tar.gz

# Set the working directory
WORKDIR /app

# Copy pom.xml and pre-fetch dependencies
COPY pom.xml ./ 
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/myscm-0.0.1-SNAPSHOT.jar /app/myscm-0.0.1-SNAPSHOT.jar



# Expose the application port
EXPOSE 8080

# Set the entry point to wait for the database and then start the application
ENTRYPOINT ["/app/wait-for-it.sh", "db-host:3306", "--", "java", "-jar", "/app/myscm-0.0.1-SNAPSHOT.jar"]

