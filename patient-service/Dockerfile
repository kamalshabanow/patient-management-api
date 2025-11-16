# Build stage
FROM gradle:8.10.2-jdk21 AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]