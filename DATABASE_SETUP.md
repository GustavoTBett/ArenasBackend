# 🗄️ Guia de Configuração do Banco de Dados - ArenasBackend

## Visão Geral

Este guia explica como configurar e executar o banco de dados PostgreSQL para o projeto **ArenasBackend** usando Docker e Docker Compose.

---

## 📋 Pré-requisitos

### Verificar instalação:
```cmd
docker --version
docker-compose --version
```

---

## 🚀 Iniciando o Banco de Dados

### 1️⃣ **Opção 1: Usando Docker Compose (Recomendado)**

#### Passo 1: Navegar até a pasta do projeto
```cmd
cd "ArenasBackend"
```

#### Passo 2: Subir os containers
```cmd
docker-compose up -d
```

**O que acontece:**
- Cria uma rede Docker chamada `api-network`
- Baixa a imagem PostgreSQL Alpine
- Inicia um container PostgreSQL na porta **5432**
- Cria um volume `postgres_data` para persistência de dados

#### Passo 3: Verificar se está rodando
```cmd
docker-compose ps
```

Você deve ver algo como:
```
NAME                COMMAND             SERVICE      STATUS      PORTS
arenas-postgres-1   postgres -c sl...   postgres     Up 2 mins   0.0.0.0:5432->5432/tcp
```

#### Passo 4: Verificar logs
```cmd
docker-compose logs -f postgres
```

Aguarde até ver mensagens como:
```
postgres_1  | database system is ready to accept connections
```

---

### 2️⃣ **Opção 2: Docker Compose com Build**

Se quiser forçar o rebuild da imagem:
```cmd
docker-compose up -d --build
```

---

## 🔌 Conectar ao Banco de Dados

### Via psql (CLI PostgreSQL)

Se tiver o PostgreSQL instalado localmente:
```cmd
psql -h localhost -U arenas -d arenas -p 5432
```

Quando solicitado, insira a senha: `arenas_pass`

### Via Docker

```cmd
docker-compose exec postgres psql -U arenas -d arenas
```

### Dados de Conexão

| Parâmetro | Valor |
|-----------|-------|
| **Host** | localhost |
| **Porta** | 5432 |
| **Database** | arenas |
| **Usuário** | arenas |
| **Senha** | arenas_pass |

---

## 🔧 Configuração da Aplicação Spring Boot

A aplicação Spring Boot deve estar configurada com os seguintes dados no `application.properties`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/arenas
spring.datasource.username=arenas
spring.datasource.password=arenas_pass
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

---

## 🛑 Parando e Removendo os Containers

### Parar sem remover
```cmd
docker-compose stop
```

### Parar e remover containers
```cmd
docker-compose down
```

### Parar, remover containers E volumes (⚠️ Deleta dados!)
```cmd
docker-compose down -v
```

---

## 🔄 Reiniciar o Banco

```cmd
docker-compose restart postgres
```

---

## 📊 Verificar Dados do Container

### Ver status do container
```cmd
docker ps -a
```

### Ver logs em tempo real
```cmd
docker-compose logs -f postgres
```

### Executar comandos dentro do container
```cmd
docker-compose exec postgres psql -U arenas -d arenas -c "SELECT version();"
```

---

## 📈 Monitorar Recursos do Container

```cmd
docker stats
```

Mostra:
- CPU usage
- Memory usage
- Network I/O
- Block I/O

---

## 🐛 Troubleshooting

### ❌ Problema: Port 5432 já está em uso

**Solução 1:** Usar uma porta diferente no `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Use 5433 em vez de 5432
```

Depois atualize `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/arenas
```

**Solução 2:** Parar o processo que usa a porta
```cmd
netstat -ano | findstr :5432
taskkill /PID <PID> /F
```

### ❌ Problema: Container não inicia

**Verificar logs:**
```cmd
docker-compose logs postgres
```

**Remover volume corrupto e tentar novamente:**
```cmd
docker-compose down -v
docker-compose up -d
```

### ❌ Problema: Erro de conexão "Connection refused"

1. Verifique se o container está rodando:
   ```cmd
   docker-compose ps
   ```

2. Aguarde o healthcheck passar (15-30 segundos):
   ```cmd
   docker-compose logs postgres
   ```

3. Verifique as credenciais em `application.properties`

### ❌ Problema: Healthcheck falhando

A configuração testa:
```cmd
pg_isready -U arenas_user -d arenas
```

**Nota:** Existe uma inconsistência no `docker-compose.yml`. O usuário criado é `arenas`, mas o healthcheck testa `arenas_user`. Isso foi corrigido.

---

## 🔐 Segurança

⚠️ **IMPORTANTE:**
- As credenciais neste `docker-compose.yml` são para **desenvolvimento local**
- **NUNCA** use essas credenciais em produção
- Para produção, use `docker-compose-prod.yml` com variáveis de ambiente:

```cmd
set POSTGRES_USER=seu_usuario_prod
set POSTGRES_PASSWORD=sua_senha_forte
docker-compose -f docker-compose-prod.yml up -d
```

---

## 💾 Backup e Restauração

### Fazer backup do banco
```cmd
docker-compose exec postgres pg_dump -U arenas arenas > backup.sql
```

### Restaurar de um backup
```cmd
docker-compose exec -T postgres psql -U arenas arenas < backup.sql
```

---

## 🔄 Workflow Completo

### Primeira execução do projeto:

```cmd
# 1. Clonar/navegar até o projeto
cd "C:\Users\Gustavo\Desktop\eng de software\eng-de-software\8 fase\projeto web\Arenas\ArenasBackend"

# 2. Subir o banco
docker-compose up -d

# 3. Aguardar healthcheck (15s)
docker-compose logs postgres

# 4. Compilar e rodar a aplicação Spring Boot
mvn clean install
mvn spring-boot:run

# 5. Acessar a API
# http://localhost:8080
```

### Encerrar tudo
```cmd
docker-compose down
```

---

## 📚 Referências

- [Docker Official Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)
- [Spring Boot Database Configuration](https://spring.io/projects/spring-boot)

---

## ✅ Checklist de Configuração

- [ ] Docker e Docker Compose instalados
- [ ] Navegou até a pasta do projeto
- [ ] Executou `docker-compose up -d`
- [ ] Verificou com `docker-compose ps`
- [ ] Testou conexão ao banco (psql ou Docker)
- [ ] Configurou `application.properties` corretamente
- [ ] Iniciou a aplicação Spring Boot
- [ ] Testou um endpoint da API

---

**Dúvidas?** Consulte o `docker-compose.yml` ou execute `docker-compose --help` para mais opções.

