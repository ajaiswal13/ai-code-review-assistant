# Architecture — AI Code Review Assistant

## 1. System Overview

The AI Code Review Assistant is a distributed web application that analyzes source code using Large Language Models (LLMs) and provides structured, real-time code review feedback.

The system is designed with:
- Clean separation of concerns
- Provider-agnostic AI layer
- Streaming-first UX
- Extensible architecture for future RAG and tool-calling capabilities

---

## 2. High-Level Architecture

The system consists of the following major components:

1. **Frontend (React + TypeScript)**
   - Provides the user interface.
   - Uploads source code.
   - Displays streaming review results.
   - Shows review history.

2. **Backend (Spring Boot 3)**
   - Exposes REST and SSE endpoints.
   - Handles authentication.
   - Coordinates the review workflow.
   - Persists application data.

3. **Core Services**
   - Authentication Service
   - Review Service
   - File Service
   - AI Orchestrator

4. **LLM Provider Layer**
   - OpenAI implementation
   - Gemini implementation
   - Common provider abstraction

5. **Database**
   - PostgreSQL for relational data.
   - pgvector (future) for embeddings and RAG.


---

## 3. Core Components

### 3.1 API Layer (Spring Boot Controllers)

Responsible for:
- Exposing REST APIs
- Managing SSE endpoints
- Request validation
- Authentication enforcement

---

### 3.2 Auth Service

Handles:
- User registration
- Login
- JWT generation & validation

Future:
- OAuth2 (Google/GitHub login)

---

### 3.3 Review Service (Core Domain Layer)

Responsible for:
- Creating review sessions
- Orchestrating AI pipeline execution
- Persisting review results
- Managing review lifecycle

This is the **business core of the system**.

---

### 3.4 File Service

Handles:
- File uploads (Java / ZIP)
- File storage (local in V1, S3-ready later)
- File parsing for AI input

Future:
- AST parsing
- Multi-module project ingestion

---

### 3.5 AI Orchestrator (Most Critical Component)

The AI Orchestrator is responsible for coordinating the entire review pipeline.

### Responsibilities

- Validate incoming requests.
- Preprocess uploaded source code.
- Construct prompts for the LLM.
- Select the appropriate LLM provider.
- Handle streaming responses.
- Parse AI responses into structured findings.
- Persist review results.
- Publish streaming events to the frontend.

### AI Review Pipeline

1. Receive uploaded source code.
2. Validate and preprocess the input.
3. Build a structured prompt.
4. Select the configured LLM provider.
5. Send the prompt to the LLM.
6. Receive streamed or complete response.
7. Parse the response into structured findings.
8. Persist findings.
9. Stream updates to the frontend.

---

### 3.6 LLM Provider Abstraction Layer

Business logic never communicates directly with an LLM provider.

Instead, it interacts with a common `LLMClient` interface.

Current implementations include:

- OpenAI Client
- Gemini Client

### Request Flow

1. Business logic invokes `LLMClient`.
2. The configured provider implementation is selected.
3. The provider communicates with the external API.
4. The response is mapped into a common internal model.
5. Business logic processes the standardized response.

### Benefits

- Prevents vendor lock-in.
- Simplifies testing.
- Supports fallback providers.
- Makes adding new providers straightforward.

---

### 3.7 Streaming Layer (SSE)

Server-Sent Events (SSE) are used to stream review progress to the frontend.

### Review Streaming Flow

1. User submits a review request.
2. Backend creates a review session.
3. AI processing begins.
4. Partial review results are generated.
5. Backend streams updates using SSE.
6. Frontend updates the UI in real time.
7. Final review is persisted and marked as completed.

---

### 3.8 Persistence Layer (PostgreSQL)

Stores:
- Users
- Reviews
- Findings
- Uploaded files

Future:
- Embeddings (pgvector)
- Prompt history
- AI traces

---

## 4. Data Flow (End-to-End)

The complete review lifecycle consists of the following steps:

1. User uploads source code.
2. Backend validates the request.
3. A review record is created.
4. Uploaded files are stored.
5. The AI Orchestrator starts processing.
6. The selected LLM analyzes the source code.
7. The AI response is converted into structured findings.
8. Findings are stored in PostgreSQL.
9. Progress updates are streamed to the frontend using SSE.
10. The review status is updated to Completed.
11. Users can access the review from their history.

---

## 5. Key Design Decisions (ADRs Summary)

### ADR-1: Use Spring Boot (Monolith First)
- Faster development
- Easier debugging
- Can evolve into microservices later

---

### ADR-2: Use SSE instead of WebSockets
- Simpler infrastructure
- One-way streaming is enough (AI → client)
- Lower operational complexity

---

### ADR-3: LLM Abstraction Layer
- Prevent vendor lock-in
- Enable fallback (OpenAI → Gemini)
- Improve testability

---

### ADR-4: PostgreSQL as primary DB
- Strong relational modeling for users/reviews
- Future-ready for pgvector embeddings

---

### ADR-5: Feature-first package structure
- Improves modularity
- Scales better than layered architecture

---

## 6. Scalability Considerations

Current V1 assumptions:
- Single region deployment
- Moderate concurrent users
- Synchronous AI processing with streaming

Future scaling:
- Queue-based AI processing (Kafka / RabbitMQ)
- Stateless backend scaling
- Distributed LLM execution
- Caching repeated analysis results

---

## 7. Security Considerations

- JWT authentication
- Input validation on file uploads
- File size limits
- Prompt injection awareness (future improvement)
- Rate limiting (future)

---

## 8. Failure Handling Strategy

- LLM timeout → fallback provider
- Streaming interruption → resume or retry
- Partial AI response handling
- Graceful degradation (return partial findings)

---

## 9. Future Architecture Evolution

### Phase 2

- Introduce Retrieval-Augmented Generation (RAG).
- Store embeddings using pgvector.
- Support project-wide code understanding.
- Improve prompt construction using repository context.

### Phase 3

- GitHub Pull Request integration.
- CI/CD pipeline integration.
- Team dashboards.
- Notifications.

### Phase 4

- AI-assisted code fixing.
- Multi-agent review workflows.
- Pull request generation.
- Organization-wide code quality analytics.
