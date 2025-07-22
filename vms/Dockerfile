# Use an official OpenJDK image as base
FROM eclipse-temurin:24-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven/Gradle build files and source code
COPY . .

# Build the application (assuming Gradle)
RUN ./gradlew clean build -x test

# Expose default Spring Boot port
EXPOSE 8090

# Run the Spring Boot app
CMD ["java", "-jar", "build/libs/vms-0.0.1-SNAPSHOT.jar"]
