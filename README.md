# ArenasBackend

Sistema backend para o **Arenas** — uma plataforma web responsiva para criação e gerenciamento de partidas de futebol. O objetivo é facilitar a organização de jogos entre amigos, permitindo criar partidas, convidar jogadores e gerenciar participações.

---

## 📋 Sobre o Projeto

O Arenas é uma aplicação web que permite aos usuários:

- 🔐 **Autenticação via Google OAuth2** — Login social seguro e prático
- ⚽ **Criar e gerenciar partidas de futebol** — Com parâmetros como local, horário e número de jogadores
- 👥 **Participar de partidas** — Entrar ou sair de jogos criados por outros usuários
- 📍 **Gerenciar locais de partida** — Cadastrar e reutilizar locais frequentes
- 📊 **Dashboard** — Visualizar estatísticas e partidas agendadas

---

## 🛠️ Stack Tecnológica

### Backend (este repositório)

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 (LTS) | Linguagem principal, aproveitando recursos modernos como Records e Pattern Matching |
| **Spring Boot** | 3.5.4 | Framework principal para desenvolvimento da API REST |
| **Spring Security** | 6.x | Segurança com OAuth2 e JWT (RSA) |
| **Spring Data JPA** | - | Persistência de dados com Hibernate |
| **QueryDSL** | 5.1.0 | Queries type-safe e dinâmicas |
| **PostgreSQL** | 17 | Banco de dados relacional |
| **Lombok** | - | Redução de boilerplate code |
| **Sentry** | 8.23.0 | Monitoramento de erros em produção |

### Infraestrutura

| Tecnologia | Descrição |
|------------|-----------|
| **Docker** | Containerização da aplicação |
| **Docker Compose** | Orquestração dos containers (app + banco) |
| **Cloudflare Tunnel** | Exposição segura da API para internet (sem necessidade de IP público ou porta aberta) |
| **Maven** | Gerenciamento de dependências e build |

> 💡 **Nota sobre o servidor:** O projeto roda em uma máquina antiga reutilizada como servidor doméstico. O Cloudflare Tunnel permite expor a aplicação de forma segura sem precisar abrir portas no roteador ou ter IP fixo.

### Segurança

- **OAuth2** com Google para autenticação social
- **JWT (JSON Web Token)** com assinatura RSA-2048
- **Refresh Tokens** armazenados no banco para renovação segura
- **Cookies HttpOnly e Secure** para proteção contra XSS
- **CORS** configurado para o frontend

---

## 📁 Estrutura do Projeto

```
src/main/java/com/projetoWeb/Arenas/
├── ArenasApplication.java          # Classe principal
├── WebConfig.java                  # Configuração CORS
├── config/                         # Configurações gerais
├── controller/                     # Controladores REST
│   ├── dashboard/                  # Endpoints do dashboard
│   ├── login/                      # Endpoints de autenticação
│   ├── match/                      # Endpoints de partidas
│   ├── user/                       # Endpoints de usuários
│   └── userMatch/                  # Endpoints de participação
├── model/                          # Entidades JPA
│   ├── User.java                   # Usuário
│   ├── Match.java                  # Partida
│   ├── UserMatch.java              # Participação em partida
│   ├── LocalMatch.java             # Local da partida
│   ├── MatchParameter.java         # Parâmetros da partida
│   └── RefreshToken.java           # Token de renovação
├── repository/                     # Repositórios Spring Data JPA
├── service/                        # Lógica de negócio
│   ├── auth/                       # Serviços de autenticação
│   ├── match/                      # Serviços de partidas
│   ├── user/                       # Serviços de usuários
│   └── userMatch/                  # Serviços de participação
└── security/                       # Configuração de segurança
    ├── SecurityConfig.java         # Configuração principal
    ├── JwtFilter.java              # Filtro JWT
    └── cookie/                     # Utilitários de cookies
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- **Docker** instalado e em funcionamento
- **Docker Compose** instalado
- Credenciais do **Google OAuth2** (Client ID e Client Secret)
- **Cloudflare Tunnel** configurado (para produção)

### 1️⃣ Gerar as Chaves RSA para JWT

Antes de subir a aplicação, você precisa gerar o par de chaves RSA:

```bash
# Gerar chave privada (formato PKCS#8, compatível com Spring)
openssl genpkey -algorithm RSA -out private.key -pkeyopt rsa_keygen_bits:2048

# Gerar chave pública a partir da privada
openssl rsa -pubout -in private.key -out public.key
```

> ⚠️ **Importante:** Nunca commite as chaves privadas no repositório!

### 2️⃣ Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto ou configure as variáveis no `docker-compose.yml`:

```env
# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/arenas
SPRING_DATASOURCE_USERNAME=arenas_user
SPRING_DATASOURCE_PASSWORD=arenas_pass

# OAuth2 Google
GOOGLE_CLIENT_ID=seu-client-id
GOOGLE_CLIENT_SECRET=seu-client-secret

# JWT (conteúdo das chaves em base64 ou path)
PUBLIC_KEY=<conteúdo-da-public.key>
PRIVATE_KEY=<conteúdo-da-private.key>

# Frontend URL (para CORS e redirect OAuth)
FRONT_URL=http://localhost:3000
```

### 3️⃣ Subir os Containers

> ⚠️ **Ordem de inicialização importante:** O banco de dados **deve subir primeiro** antes da aplicação. O Docker Compose gerencia isso automaticamente com `depends_on`, mas se você estiver subindo manualmente:

#### Opção A: Docker Compose (recomendado)

```bash
# Clone o projeto
git clone <url-do-repo>
cd ArenasBackend

# Suba todos os containers (o compose gerencia a ordem)
docker compose up -d --build
```

O Docker Compose irá:
1. 🗄️ **Primeiro:** Subir o container do PostgreSQL
2. ⏳ **Aguardar:** O banco ficar healthy (healthcheck)
3. 🚀 **Depois:** Subir a aplicação Spring Boot

#### Opção B: Subir manualmente (se necessário)

```bash
# 1. Subir APENAS o banco primeiro
docker compose up -d postgres

# 2. Aguardar o banco inicializar (cerca de 10-15 segundos)
sleep 15

# 3. Verificar se o banco está rodando
docker compose ps postgres

# 4. Subir a aplicação
docker compose up -d app
```

### 4️⃣ Verificar se está funcionando

```bash
# Ver logs da aplicação
docker compose logs -f app

# Verificar health da aplicação (local)
curl http://localhost:8080/actuator/health
```

### 5️⃣ Configurar Cloudflare Tunnel (Produção)

O Cloudflare Tunnel permite expor a aplicação para internet de forma segura, sem abrir portas no firewall:

```bash
# Instalar cloudflared (se ainda não tiver)
curl -L https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64 -o cloudflared
chmod +x cloudflared
sudo mv cloudflared /usr/local/bin/

# Fazer login no Cloudflare
cloudflared tunnel login

# Criar um tunnel
cloudflared tunnel create arenas-backend

# Configurar o tunnel para apontar para a aplicação
cloudflared tunnel route dns arenas-backend api.seudominio.com

# Rodar o tunnel (apontando para a porta da aplicação)
cloudflared tunnel run --url http://localhost:8080 arenas-backend
```

> 💡 **Dica:** Para rodar o tunnel como serviço em background:
> ```bash
> sudo cloudflared service install
> sudo systemctl start cloudflared
> ```

---

## ⚙️ Configurações Importantes

### Banco de Dados PostgreSQL

| Configuração | Valor |
|--------------|-------|
| Host | `postgres` (interno) / `localhost` (externo) |
| Porta | `5432` |
| Banco | `arenas` |
| Usuário | `arenas_user` |
| Senha | `arenas_pass` |
| Volume | `postgres_data` (persistente) |

### Portas

| Serviço | Porta | Descrição |
|---------|-------|----------|
| Spring Boot | 8080 | API REST (exposta via Cloudflare Tunnel em produção) |
| PostgreSQL | 5432 | Banco de dados (apenas acesso interno) |

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/actuator/health` | Health check (sem auth) |
| GET | `/api/user/me` | Dados do usuário logado |
| GET | `/api/match` | Listar partidas |
| POST | `/api/match` | Criar partida |
| POST | `/api/userMatch` | Entrar em partida |

---

## 🔧 Comandos Úteis

### Docker

```bash
# Subir containers
docker compose up -d --build

# Ver logs em tempo real
docker compose logs -f

# Ver logs de um serviço específico
docker compose logs -f app

# Parar containers
docker compose down

# Parar e remover volumes (⚠️ apaga dados do banco)
docker compose down -v

# Reiniciar aplicação
docker compose restart app
```

### Maven (desenvolvimento local)

```bash
# Compilar
./mvnw clean compile

# Executar testes
./mvnw test

# Rodar localmente (requer banco PostgreSQL rodando)
./mvnw spring-boot:run

# Gerar JAR
./mvnw clean package -DskipTests
```

---

## 📝 Observações

- O endpoint `/actuator/health` está configurado para acesso **sem autenticação**, permitindo healthchecks
- O DDL do banco é gerenciado automaticamente pelo Hibernate (`ddl-auto=update`)
- Em produção, considere usar **Flyway** ou **Liquibase** para migrations
- Os tokens JWT têm validade de **15 minutos**, com refresh tokens de **7 dias**
- O **Sentry** está integrado para monitoramento de erros em produção

---

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de Projeto Web.
