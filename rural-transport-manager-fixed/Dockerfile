# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the project
RUN mvn clean package

# Expose the port your app runs on
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/rural-transport-1.0-SNAPSHOT-jar-with-dependencies.jar"]
