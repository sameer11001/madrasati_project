
FROM eclipse-temurin:17-jdk-focal AS build

WORKDIR /app

COPY pom.xml mvnw ./  
COPY .mvn .mvn      


RUN apt-get update && \
    apt-get install -y --no-install-recommends maven dos2unix && \
    dos2unix mvnw && \  
    ./mvnw dependency:go-offline && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY src ./src
RUN ./mvnw package -DskipTests


FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# Create directories for static files and uploads
RUN mkdir -p /app/static /app/uploads

COPY --from=build /app/target/madrasati.jar app.jar

EXPOSE 8080

ENV PROFILE=dev

CMD ["java","-Xms256m", "-Xmx512m", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "app.jar", "--spring.profiles.active=${PROFILE}", "--debug"]
