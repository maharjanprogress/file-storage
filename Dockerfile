# syntax=docker/dockerfile:1.7

FROM maven:3.9.11-eclipse-temurin-17 AS deps
WORKDIR /build
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

FROM deps AS build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app \
    && mkdir -p /opt/jobcircle \
    && chown -R app:app /app /opt/jobcircle

COPY --from=build /build/target/*.jar /app/app.jar

ENV SERVER_PORT=9000

EXPOSE 9000
VOLUME ["/opt/jobcircle"]

USER app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
