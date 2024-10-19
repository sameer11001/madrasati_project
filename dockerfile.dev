# Step 1: Build stage
FROM eclipse-temurin:17-jdk-focal AS build

WORKDIR /app

COPY pom.xml mvnw ./

COPY .mvn .mvn

RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    ./mvnw dependency:go-offline && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY src ./src

RUN ./mvnw package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY --from=build /app/target/madrasati.jar app.jar

EXPOSE 8080

ENV PROFILE=dev

CMD ["java","-Xms256m", "-Xmx512m", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "app.jar", "--spring.profiles.active=${PROFILE}", "--debug"]
