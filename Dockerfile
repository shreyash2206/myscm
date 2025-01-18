# Stage 1: Build the application
FROM amazoncorretto:17 AS build

# Install a specific version of Maven (3.8.8)
ARG MAVEN_VERSION=3.8.8
ARG MAVEN_SHA=5fe6120f1e3347b6bd6d7f344950c0ac30cd5e0c89d4f4ff304b5be37748131c
RUN yum update -y && \
    curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz && \
    echo "${MAVEN_SHA}  maven.tar.gz" | sha256sum -c && \
    tar xzvf maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm maven.tar.gz

WORKDIR /app

# Copy only the pom.xml first to leverage Docker cache for dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/myscm-0.0.1-SNAPSHOT.jar /app/myscm-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Set the entry point for running the application
ENTRYPOINT ["java", "-jar", "/app/myscm-0.0.1-SNAPSHOT.jar"]
