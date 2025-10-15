# Stage 1: Build the project with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first (for dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the rest of the source code
COPY src ./src

# Build the project, skip tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Run the built JAR with a lightweight image
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy only the built jar from previous stage
COPY --from=build /app/target/rural-transport-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Expose port (Render injects PORT env var anyway)
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
