# Database Design

## 1. Introduction

This document defines the relational database design for the AI Code Review Assistant.

The application uses PostgreSQL as the primary datastore due to its strong transactional guarantees, mature ecosystem, and future support for vector embeddings through pgvector.

The schema is designed using normalization principles while remaining extensible for future AI capabilities.

---

# 2. Database Design Principles

The following principles guide the database design.

## Relational Modeling

Business entities are modeled using normalized relational tables.

## UUID Primary Keys

All primary keys use UUIDs.

Benefits:

- Globally unique identifiers
- Improved security
- Future distributed compatibility

## Timestamp Auditing

Every table includes:

- created_at
- updated_at

## Naming Convention

The schema follows PostgreSQL conventions.

Examples:

- created_at
- updated_at
- review_status

---

# 3. Entity Overview

Version 1 contains the following primary entities.

| Entity | Purpose |
|---------|----------|
| users | Registered application users |
| reviews | AI review sessions |
| uploaded_files | Source code uploaded for review |
| review_findings | AI-generated review findings |

Future entities:

- embeddings
- prompt_history
- ai_provider_usage
- audit_logs

---

# 4. Entity Relationships

The relationships are:

- One User has many Reviews.
- One Review has one Uploaded File (V1).
- One Review has many Review Findings.

Relationship summary:

```
User
 └── Review
      ├── Uploaded File
      └── Review Findings
```

---

# 5. Entity Definitions

## users

Stores registered users.

Suggested attributes:

- id (UUID)
- first_name
- last_name
- email
- password_hash
- created_at
- updated_at

---

## reviews

Represents a single AI review session.

Suggested attributes:

- id (UUID)
- user_id
- status
- ai_provider
- created_at
- updated_at

Status values:

- UPLOADED
- QUEUED
- ANALYZING
- COMPLETED
- FAILED

---

## uploaded_files

Stores uploaded source files.

Suggested attributes:

- id (UUID)
- review_id
- original_filename
- content_type
- file_size
- storage_path
- created_at

Version 1 stores one uploaded file per review.

The schema allows future expansion to multiple files.

---

## review_findings

Stores individual AI findings.

Suggested attributes:

- id (UUID)
- review_id
- category
- severity
- title
- description
- line_number
- suggestion
- created_at

Categories include:

- Bug
- Performance
- Security
- Code Quality
- Design

Severity levels:

- LOW
- MEDIUM
- HIGH
- CRITICAL

---

# 6. Normalization Decisions

Review findings are stored in a dedicated table instead of JSON.

Reasons:

- Efficient querying
- Better indexing
- Easier reporting
- Simpler filtering
- Future analytics support

Uploaded files are separated from reviews to support future multi-file uploads.

---

# 7. Indexing Strategy

Version 1 indexes include:

users

- email (unique)

reviews

- user_id
- status
- created_at

review_findings

- review_id
- severity
- category

uploaded_files

- review_id

Additional indexes will be introduced based on production usage.

---

# 8. Referential Integrity

Foreign keys enforce relationships.

Examples:

- reviews.user_id → users.id
- uploaded_files.review_id → reviews.id
- review_findings.review_id → reviews.id

Cascade behavior will be defined during implementation.

---

# 9. Future Schema Evolution

The schema is designed for future expansion.

Potential additions:

## embeddings

Stores vector embeddings using pgvector.

Purpose:

Repository-aware Retrieval-Augmented Generation (RAG).

---

## prompt_history

Stores prompts sent to AI providers.

Purpose:

Debugging and prompt engineering.

---

## ai_provider_usage

Tracks provider usage.

Purpose:

Cost monitoring and analytics.

---

## audit_logs

Tracks important system events.

Purpose:

Security and auditing.

---

# 10. ER Diagram

Version 1 relationship overview:

```
users
  │
  └──── reviews
            │
            ├──── uploaded_files
            │
            └──── review_findings
```

A Mermaid ER diagram will replace this representation during implementation.

---

# 11. Database Migration Strategy

Schema changes will be managed using Flyway.

Guidelines:

- Never modify executed migrations.
- Create a new migration for every schema change.
- Keep migrations backward compatible whenever possible.

---

# 12. Future Considerations

Future versions may introduce:

- pgvector
- Repository indexing
- Soft deletes
- Audit history
- Multi-file reviews
- AI provider analytics
- Team ownership