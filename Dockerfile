# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy the build wrapper and dependencies definition
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Download dependencies (allows layer caching)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copy application source code and build it
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Run as non-root user for security best practices (often required by AWS ECS/EKS)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
