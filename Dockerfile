# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom first to leverage Docker layer caching for dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 5000

# -Xmx caps heap size to fit inside Render's free-tier 512MB container.
# Adjust -Xmx down (e.g. 300m) if you still see OOM kills.
ENTRYPOINT ["java", "-Xmx350m", "-Xss256k", "-XX:+UseSerialGC", "-jar", "app.jar"]