FROM maven:3.9.8-eclipse-temurin-21 AS builder
ARG SENTRY_AUTH_TOKEN
ENV SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN}
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# --------------------------------------------------
# Segunda fase: imagem final leve
# --------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

# Instala o curl para baixar o agente do Sentry
RUN apk add --no-cache curl

WORKDIR /app

# Copia o JAR da aplicação gerado no build
COPY --from=builder /app/target/*.jar app.jar

# 🔽 Baixa o agente do Sentry (ajuste a versão se quiser)
RUN curl -fSL https://repo1.maven.org/maven2/io/sentry/sentry-opentelemetry-agent/8.23.0/sentry-opentelemetry-agent-8.23.0.jar -o /app/sentry-agent.jar
ENV OTEL_EXPORTER_OTLP_ENDPOINT=https://o4510157245186048.ingest.us.sentry.io:443
ENV OTEL_EXPORTER_OTLP_HEADERS="Authorization=Sentry ${SENTRY_AUTH_TOKEN}"
ENV OTEL_SERVICE_NAME=ArenasBackend

EXPOSE 8080

# Executa com o Java Agent
ENTRYPOINT ["java", "-javaagent:/app/sentry-agent.jar", "-jar", "app.jar"]

