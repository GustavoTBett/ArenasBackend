FROM maven:3.9.8-eclipse-temurin-21 AS builder
ARG SENTRY_AUTH_TOKEN
ENV SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN}
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN curl -L -o /app/sentry-agent.jar \
  https://repo1.maven.org/maven2/io/sentry/sentry-opentelemetry-agent/8.23.0/sentry-opentelemetry-agent-8.23.0.jar
ENV SENTRY_AUTO_INIT=false
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/app/sentry-agent.jar", "-jar", "app.jar"]






