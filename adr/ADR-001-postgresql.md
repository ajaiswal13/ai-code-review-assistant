# ADR-001: PostgreSQL as the Primary Database

## Status

Accepted

---

## Context

The application requires persistent storage for users, reviews, uploaded files, and AI-generated findings.

The database should support relational data modeling while also enabling future AI capabilities such as Retrieval-Augmented Generation (RAG) and vector search.

---

## Decision

PostgreSQL will be used as the primary relational database.

Future AI capabilities will leverage the pgvector extension for storing and querying vector embeddings.

---

## Alternatives Considered

### PostgreSQL

**Pros**

- Strong ACID compliance.
- Excellent relational modeling.
- Mature indexing capabilities.
- Native JSON support.
- Seamless integration with Spring Boot and Flyway.
- Supports pgvector for future AI features.

**Cons**

- Slightly larger operational footprint than lightweight databases.

---

### MySQL

**Pros**

- Mature relational database.
- Excellent Spring ecosystem support.
- Strong transactional capabilities.

**Cons**

- No first-class equivalent to pgvector.
- Future semantic search would likely require an additional vector database.

---

### MongoDB

**Pros**

- Flexible document model.
- Easy schema evolution.

**Cons**

- Poor fit for highly relational data.
- Weaker support for relational queries and referential integrity.

---

## Consequences

### Positive

- Strong relational consistency.
- Excellent support for future AI features.
- Single database for relational and vector data during early project phases.
- Simplified operational architecture.

### Negative

- Future scaling requirements may eventually justify a dedicated vector database.

---

## Future Considerations

If semantic search grows significantly, embeddings may be migrated to a specialized vector database while PostgreSQL continues managing transactional data.