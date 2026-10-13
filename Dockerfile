# -------------------------------------------------------------
# Stage 1: Build Java Spring Boot Application with Maven
# -------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production executable JAR (skipping tests for fast container packaging)
RUN mvn clean package -DskipTests -B

# -------------------------------------------------------------
# Stage 2: Ultra-lightweight JRE Runtime for Render Deployment
# -------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Non-root security user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy built artifact from builder
COPY --from=builder /build/target/*.jar app.jar

# Render supplies $PORT environment variable dynamically
ENV PORT=8080
EXPOSE 8080

# Run Spring Boot service
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${PORT:-8080} -jar app.jar"]
