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
RUN curl -L -o sentry-agent.jar \
    https://repo1.maven.org/maven2/io/sentry/sentry-java-agent/8.23.0/sentry-java-agent-7.14.0.jar

EXPOSE 8080

# Executa com o Java Agent
ENTRYPOINT ["java", "-javaagent:/app/sentry-agent.jar", "-jar", "app.jar"]
