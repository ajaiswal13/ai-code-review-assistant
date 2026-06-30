# AI Code Review Assistant

> 🚀 A production-grade AI-powered code review platform that analyzes source code using LLMs (OpenAI / Gemini) and provides structured, real-time feedback via streaming.

---

## 📊 Project Status

![Status](https://img.shields.io/badge/status-in%20development-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![AI](https://img.shields.io/badge/AI-LLM%20Powered-purple)
![Architecture](https://img.shields.io/badge/architecture-clean--modular-brightgreen)

---

## 🧠 Why This Project?

Modern code reviews are:

- Slow ⏳
- Inconsistent ⚠️
- Dependent on senior availability 👨‍💻
- Repetitive 🔁

Developers already use LLMs, but:

- ❌ Lack structured outputs
- ❌ No project context
- ❌ Manual copy-paste workflow
- ❌ No history or traceability

This system solves that by providing **automated, structured, streaming code reviews**.

---

## 🎯 Vision

Build an AI-native code review system that:
- integrates with developer workflows
- understands full code context
- provides structured, explainable feedback
- supports multiple LLM providers
- evolves into a RAG-powered engineering assistant

---

## ✨ Features (V1)

<details>
<summary>📂 Code Upload</summary>

- Java file upload
- ZIP project upload (future-ready)
- Paste code support

</details>

<details>
<summary>🤖 AI Code Review</summary>

- Bug detection
- Performance analysis
- Security checks
- Clean code suggestions
- Design improvements

</details>

<details>
<summary>📡 Real-Time Streaming</summary>

- Server-Sent Events (SSE)
- Live analysis progress
- Incremental feedback display

</details>

<details>
<summary>📊 Structured Output</summary>

- Severity-based classification
- Categorized findings
- Line-level suggestions

</details>

<details>
<summary>🧠 AI Abstraction Layer</summary>

- OpenAI support
- Gemini support
- Pluggable LLM architecture

</details>

---

## 🏗️ High-Level Architecture
Frontend (React + TS)
        │
        ▼
Backend (Spring Boot 3)
        │
┌───────┼────────┐
▼       ▼        ▼
Auth  Review  AI Layer
Service Service (LLM Abstraction)
        │
        ▼
PostgreSQL + pgvector



📌 Detailed architecture: `docs/architecture.md`

---

## 🛠 Tech Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Flyway
- SSE (Streaming)

### Frontend
- React
- TypeScript
- Vite

### AI Layer
- OpenAI API
- Google Gemini API
- Custom LLM abstraction layer

### Infrastructure
- Docker
- Docker Compose
- pgvector (future RAG support)

---

## 🚀 Getting Started

### 1. Clone Repository

```bash
git clone https://github.com/<your-repo>/ai-code-review-assistant.git
cd ai-code-review-assistant

### 2. Start Infrastructure

```bash
docker-compose up -d

### 3. Run Backend

```bash
cd backend
./mvnw spring-boot:run


### 4. Run Frontend

```bash
cd frontend
npm install
npm run dev

📁 Project Structure

backend/   → Spring Boot backend
frontend/  → React frontend
docs/      → Architecture & design docs
adr/       → Architecture decision records
docker/    → Infrastructure setup
scripts/   → Utility scripts