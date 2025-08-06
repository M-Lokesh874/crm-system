# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pre-built JAR (we'll build it locally first)
COPY backend/discovery-server/target/discovery-server-1.0-SNAPSHOT.jar ./discovery-server.jar

# Expose the port
EXPOSE 8761

# Set JVM options for better performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar discovery-server.jar"] 