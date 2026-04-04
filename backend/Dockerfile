# STAGE 1: Bauen der Applikation im Maven-Container
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Die Projektdateien kopieren
COPY pom.xml .
COPY src ./src

# Mit Maven die .jar-Datei bauen
RUN mvn clean package -DskipTests

# STAGE 2: Ausführen der App in einem schlanken JRE-Container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Ziehe die fertige .jar von Stage 1 ab
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
