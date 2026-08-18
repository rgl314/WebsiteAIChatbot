<div align="center">

# 💬 Website AI Chatbot

### A Multi-Tenant Website Chatbot built with Spring Boot, Spring AI, Gemini, Qdrant, MySQL, Playwright, Flyway, Spring Security & Docker

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-brightgreen?style=for-the-badge&logo=springboot)
![Spring AI](https://img.shields.io/badge/Spring_AI-2.0.0-6DB33F?style=for-the-badge&logo=spring)
![Gemini](https://img.shields.io/badge/Google_Gemini-AI-4285F4?style=for-the-badge&logo=google)
![MySQL](https://img.shields.io/badge/MySQL-8-blue?style=for-the-badge&logo=mysql)
![Qdrant](https://img.shields.io/badge/Qdrant-Vector_Database-red?style=for-the-badge)
![Playwright](https://img.shields.io/badge/Playwright-Browser_Automation-2EAD33?style=for-the-badge&logo=playwright)
![Flyway](https://img.shields.io/badge/Flyway-Database_Migrations-cc0000?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Spring_Security-7.x-6DB33F?style=for-the-badge&logo=springsecurity)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

</div>

---

# 📖 About

A **multi-tenant AI website chatbot platform** built with **Spring Boot** and **Spring AI**.

The system allows different websites to register their site, ingest their website content, store the content as vector embeddings in **Qdrant**, and provide an AI chatbot that answers questions using the website's own knowledge.

The chatbot uses **Google Gemini** for language generation and embeddings, **Qdrant** for vector search, and **MySQL** for site registration, conversations, administrators, and persistent chat memory.

JavaScript-rendered websites are processed using **Playwright**, allowing the ingestion system to extract content from modern React, Vue, Angular, and other client-rendered websites.

The project demonstrates practical implementation of modern AI application concepts including **RAG, vector databases, embeddings, LLM integration, persistent AI memory, tenant isolation, website ingestion, browser automation, Spring Security, database migrations, and embeddable JavaScript widgets**.

---

# ✨ Features

* 🤖 Google Gemini-powered AI chatbot
* 🧠 Retrieval-Augmented Generation (RAG)
* 🔎 Semantic similarity search using Qdrant
* 🧩 Gemini text embeddings for website knowledge
* 🌐 Website content ingestion using Playwright
* ⚛️ Support for JavaScript-rendered websites
* ✂️ Automatic document chunking before embedding
* 🏢 Multi-site / multi-tenant architecture
* 🔐 Site-specific public keys for chatbot identification
* 🌍 Origin-based website authorization
* 💾 Persistent conversation memory using MySQL
* 🧠 Spring AI `MessageChatMemoryAdvisor`
* 📚 Spring AI `QuestionAnswerAdvisor`
* 🎯 Site-specific Qdrant metadata filtering
* 👤 MySQL-backed administrator authentication
* 🔑 Spring Security Basic Authentication for admin APIs
* 🔒 Role-based admin authorization
* 🗄️ MySQL persistence with Spring Data JPA
* 🔄 Flyway database migrations
* ⚠️ Global API exception handling
* 💬 Embeddable JavaScript chatbot widget
* 📱 Responsive chatbot UI
* 🐳 Docker / Docker Compose support

---

# 🏗️ Application Flow

## Main flow

1. Administrator registers a website.
2. Application generates a unique `siteId` and public key.
3. Website information is stored in MySQL.
4. Administrator starts website ingestion.
5. Playwright opens the registered website in a headless Chromium browser.
6. JavaScript-rendered content is extracted from the page.
7. Extracted content is converted into Spring AI `Document` objects.
8. Documents are split into smaller chunks.
9. Gemini generates embeddings for each chunk.
10. Embeddings are stored in Qdrant with the corresponding `siteId`.
11. A visitor opens the website chatbot widget.
12. The widget sends the public key, conversation ID, and user message to the backend.
13. The backend validates the website origin and resolves the registered site.
14. Spring AI retrieves conversation memory from MySQL.
15. Spring AI retrieves relevant website knowledge from Qdrant.
16. Gemini receives the user question, conversation context, and retrieved website knowledge.
17. Gemini generates the final response.
18. The response is returned to the JavaScript chatbot widget.

---

# 🧠 RAG Architecture

```text
                         User Question
                               │
                               ▼
                    ┌────────────────────┐
                    │   ChatController   │
                    └─────────┬──────────┘
                              │
                    Public Key + Origin
                              │
                              ▼
                 ┌─────────────────────────┐
                 │ Origin Validation       │
                 │ + Site Resolution       │
                 └────────────┬────────────┘
                              │
                  ┌───────────┴───────────┐
                  │                       │
                  ▼                       ▼
            Chat Memory                Qdrant
               MySQL                Site Knowledge
                  │                       │
                  │                 Similarity Search
                  │                       │
                  └───────────┬───────────┘
                              │
                              ▼
                    QuestionAnswerAdvisor
                              │
                              ▼
                         ChatClient
                              │
                              ▼
                       Google Gemini
                              │
                              ▼
                           Response
````

---

# 🌐 Website Ingestion Flow

```text
          Registered Website URL
                    │
                    ▼
             Playwright
                    │
             Chromium Browser
                    │
                    ▼
            Execute JavaScript
                    │
                    ▼
             Rendered Web Page
                    │
                    ▼
               Extract Text
                    │
                    ▼
              Spring Document
                    │
                    ▼
             TokenTextSplitter
                    │
                    ▼
              Text Chunks
                    │
                    ▼
          Gemini Embedding Model
                    │
                    ▼
           3072-Dimensional Vector
                    │
                    ▼
                  Qdrant
                    │
          Metadata: siteId
```

---

# 🔎 Retrieval Flow

When a user asks a question:

```text
User
 │
 │ "What projects are listed?"
 ▼
ChatClient
 │
 ├── MessageChatMemoryAdvisor
 │       │
 │       └── MySQL conversation history
 │
 └── QuestionAnswerAdvisor
         │
         └── Qdrant similarity search
                 │
                 └── siteId filter
                         │
                         ▼
                  Relevant Documents
                         │
                         ▼
                       Gemini
                         │
                         ▼
                      Answer
```

The chatbot does not rely only on Gemini's general knowledge. Website-specific questions are grounded using the indexed website content.

---

# 🔐 Multi-Tenant Architecture

Each registered website receives:

```text
siteId
publicKey
domain
```

Example:

```text
siteId:
site-c940c842

publicKey:
pk_96a828c3aee54d6b832a7c0c873062dd

domain:
https://www.raguls4.vercel.app
```

Website knowledge is stored in Qdrant with:

```text
siteId = site-c940c842
```

When retrieving information, the chatbot applies the corresponding site filter.

This prevents knowledge belonging to one website from being retrieved for another website.

---

# 🔐 Website Origin Validation

The chatbot widget sends:

```http
X-Chatbot-Public-Key: pk_...
Origin: https://example.com
```

The backend:

```text
Public Key
    ↓
Find Site
    ↓
Compare request Origin
    ↓
Registered Domain
    ↓
Allowed?
```

Example:

```text
Registered:
https://www.raguls4.vercel.app

Request:
https://www.raguls4.vercel.app

                ↓

             ✅ Allowed
```

A different website using the same public key is rejected.

---

# 🔑 Admin Authentication Flow

Administrative endpoints are protected using **Spring Security** and database-backed users.

```text
Admin
 │
 │ Basic Authentication
 ▼
Spring Security
 │
 ▼
DaoAuthenticationProvider
 │
 ▼
CustomUserDetailsService
 │
 ▼
MySQL admin_users
 │
 ▼
PasswordEncoder
 │
 ▼
ROLE_ADMIN
 │
 ▼
Admin API
```

Public chatbot requests do not require administrator credentials.

---

# 💾 Conversation Memory

The chatbot stores conversation history using Spring AI's JDBC chat memory repository.

```text
User Message
     │
     ▼
MessageChatMemoryAdvisor
     │
     ▼
MySQL
     │
     ▼
SPRING_AI_CHAT_MEMORY
```

Conversation identifiers are generated by the widget using UUIDs and remain site-specific on the client side.

Conversation ownership is additionally maintained through the application's `conversations` table.

---

# 📌 Results

## Website Registration

```http
POST /api/sites
```

```json
{
  "name": "Ragul Portfolio",
  "domain": "https://raguls4.vercel.app/"
}
```

Example response:

```json
{
  "siteId": "site-64c1f1ff",
  "publicKey": "pk_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "name": "Ragul Portfolio",
  "domain": "https://raguls4.vercel.app/"
}
```

---

## Website Ingestion

```http
POST /api/sites/{siteId}/ingest
```

Example response:

```json
{
  "siteId": "site-64c1f1ff",
  "pagesDiscovered": 1,
  "pagesIndexed": 1,
  "chunksCreated": 2,
  "failedPages": 0,
  "status": "COMPLETED"
}
```

---

## Chat

```http
POST /api/chat
```

Headers:

```text
X-Chatbot-Public-Key: pk_xxxxxxxxx
Content-Type: application/json
```

Request:

```json
{
  "conversationId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "What do you know about this website?"
}
```

Response:

```json
{
  "response": "This website is the personal portfolio of Ragul S..."
}
```

---

# 🌐 REST API

## Public APIs

| Method | Endpoint                         | Description                                 |
| ------ | -------------------------------- | ------------------------------------------- |
| POST   | `/api/chat`                      | Chat with the website-specific AI assistant |
| GET    | `/widget.js`                     | Serve embeddable chatbot widget             |

---

## Admin APIs

All site-management APIs require `ROLE_ADMIN`.

| Method | Endpoint                     | Description            |
| ------ | ---------------------------- | ---------------------- |
| POST   | `/api/sites`                 | Register a website     |
| POST   | `/api/sites/{siteId}/ingest` | Ingest website content |

---

# 📥 Sample Chat Request

```json
{
  "conversationId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "What projects are available?"
}
```

Header:

```text
X-Chatbot-Public-Key: pk_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

---

# 📤 Sample Chat Response

```json
{
  "response": "The website contains several projects including a URL Shortener, Chatter, Digital Banking System, and Secure GatePass Management System."
}
```

---

# 🧩 Embeddable Chatbot Widget

The chatbot can be embedded into a website using a single script:

```html
<script
    src="https://your-chatbot-domain.com/widget.js"
    data-site-key="pk_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx">
</script>
```

The widget automatically:

* Loads the site's chatbot configuration
* Creates a site-specific conversation ID
* Displays the chatbot UI
* Sends messages to the Spring Boot backend
* Displays AI responses
* Preserves the conversation ID across page refreshes

---

# 🛠️ Tech Stack

| Category           | Technology                           |
| ------------------ | ------------------------------------ |
| Language           | Java 21                              |
| Backend Framework  | Spring Boot 4.1.0                    |
| AI Framework       | Spring AI 2.0.0                      |
| AI Model           | Google Gemini                        |
| Embedding Model    | Gemini Embedding                     |
| Vector Database    | Qdrant                               |
| Database           | MySQL 8                              |
| ORM                | Spring Data JPA / Hibernate          |
| AI Memory          | Spring AI JDBC Chat Memory           |
| Website Rendering  | Playwright                           |
| Security           | Spring Security                      |
| Authentication     | HTTP Basic Authentication            |
| Password Hashing   | BCrypt / Delegating Password Encoder |
| Database Migration | Flyway                               |
| Frontend Widget    | Vanilla JavaScript                   |
| Containerization   | Docker / Docker Compose              |
| Utilities          | Lombok                               |

---

# 📂 Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.ragul.ChatBot
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── util
│   │
│   └── resources
│       ├── application.yml
│       ├── static
│       │   └── widget.js
│       └── db
│           └── migration
│               ├── V1__create_application_tables.sql
│               └── V2__create_admin_users.sql
│
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

# 🗄️ Database

Flyway manages the application's database schema.

## Main Tables

### `sites`

Stores registered websites and chatbot configuration.

```text
id
site_id
public_key
name
domain
```

---

### `conversations`

Associates conversations with their registered websites.

```text
id
site_id
conversation_id
```

The pair:

```text
site_id + conversation_id
```

is unique.

---

### `admin_users`

Stores administrative users.

```text
id
username
password
role
enabled
```

Passwords are stored using a password encoder.

---

### `SPRING_AI_CHAT_MEMORY`

Spring AI's JDBC repository stores conversation messages.

```text
conversation_id
content
type
timestamp
sequence_id
```

---

# 🧠 Vector Database

Qdrant stores embeddings generated from website content.

Collection:

```text
website_knowledge
```

Each vector contains the associated website identifier as metadata:

```text
siteId
```

This allows site-specific retrieval.

Example:

```text
Question
   ↓
Embedding
   ↓
Qdrant similarity search
   ↓
siteId filter
   ↓
Relevant website chunks
```

---

# ⚙️ Configuration

Configure environment variables:

```properties
DB_PASSWORD=password
GEMINI_API_KEY=your-gemini-api-key
ADMIN_USERNAME=admin
ADMIN_PASSWORD=your-admin-password
```

> Never commit real API keys, passwords, or `.env` files to the repository. Use `.env.example` for documentation.

---

# ⚡ application.yml Example

```yaml
server:
  port: 8080

spring:
  application:
    name: ChatBot

  datasource:
    url: jdbc:mysql://localhost:3307/chatbot
    username: root
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

  flyway:
    enabled: true
    locations: classpath:db/migration

  ai:
    google:
      genai:
        api-key: ${GEMINI_API_KEY}

        chat:
          model: gemini-3.5-flash
          temperature: 0.7

        embedding:
          api-key: ${GEMINI_API_KEY}

          text:
            model: gemini-embedding-001

    vectorstore:
      qdrant:
        host: localhost
        port: 6334
        collection-name: website_knowledge
        use-tls: false
        initialize-schema: true

    chat:
      memory:
        repository:
          jdbc:
            initialize-schema: always

app:
  security:
    development-origins:
      - http://localhost:5173
      - http://localhost:3000

  admin:
    username: ${ADMIN_USERNAME}
    password: ${ADMIN_PASSWORD}
```

---

# 🚀 Running Locally

## Clone Repository

```bash
git clone https://github.com/<your-username>/WebsiteAIChatbot.git
cd WebsiteAIChatbot
```

---

## Configure Environment Variables

Create a `.env` file or configure the variables in your environment:

```properties
DB_PASSWORD=your-password
GEMINI_API_KEY=your-gemini-api-key
ADMIN_USERNAME=admin
ADMIN_PASSWORD=your-admin-password
```

---

## Start MySQL & Qdrant

Make sure Docker is installed and running.

Start the required services:

```bash
docker compose up -d
```

---

## Run Application

Using Maven:

```bash
./mvnw spring-boot:run
```

or:

```bash
mvn spring-boot:run
```

---

# 🐳 Docker

Build and start all services:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up -d
```

View running containers:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

Stop containers:

```bash
docker compose down
```

---

# 🧩 Docker Services

| Service | Container         | Port          | Purpose                        |
| ------- | ----------------- | ------------- | ------------------------------ |
| MySQL   | `mysql_chatbot`   | `3307`        | Application database           |
| Qdrant  | `qdrant_chatbot`  | `6333 / 6334` | Vector database                |
| Backend | `chatbot_backend` | `8080`        | Spring Boot API and AI service |

---

# 🔄 AI Request Architecture

```text
                    ┌───────────────┐
                    │    Website    │
                    │    Visitor   │
                    └───────┬───────┘
                            │
                       widget.js
                            │
                            ▼
                  ┌──────────────────┐
                  │   Spring Boot    │
                  └────────┬─────────┘
                           │
                  Origin + Public Key
                           │
                           ▼
                 ┌────────────────────┐
                 │ Site Validation     │
                 └──────────┬─────────┘
                            │
               ┌────────────┴────────────┐
               │                         │
               ▼                         ▼
         MySQL Memory                Qdrant
               │                         │
               │                    Site Filter
               │                         │
               └────────────┬────────────┘
                            ▼
                     Spring AI
                            │
                 QuestionAnswerAdvisor
                            │
                            ▼
                       ChatClient
                            │
                            ▼
                      Google Gemini
                            │
                            ▼
                         Response
```

---

# 🛡️ Error Handling

The application uses a global exception handler to return consistent API errors.

Example:

```json
{
  "timestamp": "2026-08-18T16:07:12",
  "status": 422,
  "error": "INGESTION_FAILED",
  "message": "Website ingestion failed.",
  "path": "/api/sites/site-c940c842/ingest"
}
```

Common responses include:

```text
400 → VALIDATION_ERROR
401 → Unauthorized
403 → ORIGIN_NOT_ALLOWED
404 → SITE_NOT_FOUND
422 → INGESTION_FAILED
500 → INTERNAL_SERVER_ERROR
```

---

# 🔐 Security Model

The application separates **public chatbot access** from **administrative access**.

### Public chatbot

```text
publicKey
+
Origin
+
conversationId
```

The backend verifies that the website origin matches the registered site.

### Admin APIs

```text
Username
+
Password
+
ROLE_ADMIN
```

are required for site registration and website ingestion.

This provides two separate security boundaries:

```text
Website Visitor
      ↓
Origin + Public Key

Administrator
      ↓
Spring Security + ADMIN role
```

---

# 🧪 Testing

Run the Maven test suite:

```bash
./mvnw test
```

or:

```bash
mvn test
```

The application can also be manually tested through Postman or the embedded chatbot widget.

---

# ⚠️ Current Limitations

The current ingestion pipeline processes the configured website URL as a rendered page using Playwright.

The current implementation does not yet provide:

* Automatic multi-page website crawling
* Incremental website re-indexing
* Background ingestion jobs
* PDF/document ingestion
* Advanced retrieval reranking
* Streaming AI responses

These can be added as future improvements.

---

# 📈 Future Enhancements

* 🕷️ Multi-page website crawling
* 🔄 Incremental knowledge re-indexing
* 📄 PDF and document ingestion
* ⚡ Background ingestion jobs
* 🔐 JWT/OAuth2 admin authentication
* 👥 Multiple administrators with site-level permissions
* 🎨 More widget customization
* 🌊 Streaming AI responses
* 📊 Usage and token analytics
* 🧪 Expanded unit and integration test coverage
* 📚 Swagger/OpenAPI documentation
* 📈 Monitoring with Prometheus and Grafana
* ☁️ Production deployment
* 🐳 Production container hardening
* ☸️ Kubernetes deployment

---

# 🎯 Project Goals

This project was built to explore practical implementation of:

```text
Spring Boot
     +
Spring AI
     +
Large Language Models
     +
Embeddings
     +
Vector Databases
     +
RAG
     +
AI Memory
     +
Multi-Tenant Architecture
     +
Website Ingestion
     +
Spring Security
```

The goal is to understand how modern AI capabilities can be integrated into a conventional backend application rather than building an isolated AI demo.

---

# 🤝 Contributing

Contributions, suggestions, and improvements are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push the branch
5. Open a Pull Request

---

# 📄 License

This project is licensed under the **MIT License**.

---

<div align="center">

### ⭐ If you found this project useful, consider giving it a star!

Built with ❤️ using **Spring Boot**, **Spring AI**, **Gemini**, **Qdrant**, **MySQL**, **Playwright**, **Flyway**, **Spring Security**, and **Docker**

</div>
