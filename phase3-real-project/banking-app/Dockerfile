# Multi-stage build for Banking App
# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Nếu dùng Maven Wrapper:
# COPY .mvn ./.mvn
# COPY mvnw .
# RUN chmod +x mvnw && ./mvnw clean package -DskipTests
# Nếu dùng Maven:
RUN apk add --no-cache maven && mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
