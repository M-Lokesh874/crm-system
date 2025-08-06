FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean install -DskipTests

# Expose port
EXPOSE 8081

# Run the application
CMD ["./mvnw", "spring-boot:run"] 