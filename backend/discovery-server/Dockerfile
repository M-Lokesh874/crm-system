# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Build the application from the root directory
RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests -pl backend/discovery-server

# Expose the port
EXPOSE 8761

# Set JVM options for better performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar backend/discovery-server/target/discovery-server-1.0-SNAPSHOT.jar"] 