# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY backend/pom.xml backend/
COPY backend/crm-common/pom.xml backend/crm-common/
COPY backend/discovery-server/pom.xml backend/discovery-server/

# Copy source code
COPY backend/crm-common/src backend/crm-common/src
COPY backend/discovery-server/src backend/discovery-server/src

# Build the application
RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests -pl backend/discovery-server

# Expose the port
EXPOSE 8761

# Set JVM options for better performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar backend/discovery-server/target/discovery-server-1.0-SNAPSHOT.jar"] 