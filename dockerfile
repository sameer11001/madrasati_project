# Build stage
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app
COPY . .
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    ./mvnw package -DskipTests && \
    ls -la /app/target && \
    apt-get clean && rm -rf /var/lib/apt/lists/*
RUN apt-get autoremove


# Run stage
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /app/target/madrasati-app.jar app.jar
EXPOSE 8080
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "app.jar"]

