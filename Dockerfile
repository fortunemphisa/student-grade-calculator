# ─────────────────────────────────────────────
# Stage 1: Build
# ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-11 AS build

WORKDIR /app

# Copy dependency manifests first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the WAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─────────────────────────────────────────────
# Stage 2: Run
# ─────────────────────────────────────────────
FROM tomcat:9.0-jdk11-temurin

# Remove the default Tomcat webapps to keep the image clean
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy the WAR as the ROOT application so it is served at /
COPY --from=build /app/target/grade-calculator.war /usr/local/tomcat/webapps/ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat (default CMD from the base image, kept explicit for clarity)
CMD ["catalina.sh", "run"]
