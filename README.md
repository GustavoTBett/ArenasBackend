# ArenasBackend

Sistema backend para o Arenas — plataforma web responsiva para criação e gerenciamento de partidas de futebol.

## Tecnologias usadas

- Java 21 + Spring Boot 3.5.x
- PostgreSQL
- Docker e Docker Compose
- Nginx (reverso proxy)

## Estrutura do projeto

- API Spring Boot rodando na porta 9999
- Banco PostgreSQL com usuário e senha configurados via Docker Compose
- Nginx expõe a API na porta 9999, fazendo proxy reverso

---

## Como rodar o projeto

### Pré-requisitos

- Docker instalado e em funcionamento
- Docker Compose instalado
- Porta 9999 livre no host

### Passos para subir a aplicação

1. Clone o projeto:

   ```bash
   git clone <url-do-repo>
   cd ArenasBackend
   ```

2. Construa e suba os containers:

   ```bash
   docker compose up -d --build
   ```

## Configurações importantes

- Banco PostgreSQL configurado no Docker Compose com:

    - Banco: `arenas`
    - Usuário: `arenas_user`
    - Senha: `arenas_pass`
    - Dados persistem em volume `postgres_data`

- Variáveis de ambiente para a API no Docker Compose:

    - `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/arenas`
    - `SPRING_DATASOURCE_USERNAME=arenas_user`
    - `SPRING_DATASOURCE_PASSWORD=arenas_pass`
    - `SPRING_JPA_SHOW_SQL=false`

- Nginx está configurado para expor a API na porta 9999

---

## Observações

- O endpoint `/actuator/health` deve estar configurado para acesso sem autenticação, para que o healthcheck funcione.  
- Ajuste os valores das variáveis de ambiente conforme necessário para seu ambiente.  
- Para parar a aplicação:

   ```bash
   docker compose down
   ```
