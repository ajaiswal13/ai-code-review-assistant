# API Design

## 1. Introduction

This document defines the REST API design for the AI Code Review Assistant.

The APIs follow RESTful principles, are versioned from the beginning, and are designed to support future enhancements without introducing breaking changes.

Version 1 focuses on authentication, code review management, streaming AI responses, and review history.

---

# 2. API Design Principles

The following principles apply to all APIs in the system.

## Resource-Oriented Design

APIs represent resources rather than actions.

Examples:

- `/api/v1/reviews`
- `/api/v1/reviews/{reviewId}`
- `/api/v1/auth/login`

Avoid verb-based endpoints such as:

- `/createReview`
- `/startReview`
- `/getReview`

---

## Versioning Strategy

All APIs are versioned.

Current version:

```
/api/v1
```

Future versions will be introduced without breaking existing clients.

---

## Stateless Communication

The backend remains stateless.

Every request contains all required information for processing.

Authentication is performed using JWT tokens.

---

## Consistent Response Structure

Successful responses return the requested resource.

Errors follow a standardized error format.

---

# 3. Resource Model

The primary resources exposed by the API are:

| Resource | Description |
|----------|-------------|
| Authentication | User registration and login |
| Users | Authenticated user information |
| Reviews | AI code review sessions |
| Findings | AI-generated review findings |

---

# 4. Review Lifecycle

Each review progresses through predefined states.

1. **UPLOADED**
   - Source code successfully uploaded.

2. **QUEUED**
   - Waiting for AI processing.

3. **ANALYZING**
   - AI provider is generating findings.

4. **COMPLETED**
   - Review successfully completed.

5. **FAILED**
   - Review could not be completed.

State transitions occur in the following order:

```
UPLOADED
    ↓
QUEUED
    ↓
ANALYZING
    ↓
COMPLETED

OR

ANALYZING
    ↓
FAILED
```

---

# 5. Authentication Flow

Authentication is based on JSON Web Tokens (JWT).

Flow:

1. User registers.
2. User logs in.
3. Backend validates credentials.
4. Backend generates JWT.
5. Client stores JWT securely.
6. JWT is sent in the Authorization header for protected requests.
7. Backend validates JWT before processing requests.

---

# 6. Authentication APIs

## Register User

| Property | Value |
|----------|-------|
| Method | POST |
| Endpoint | `/api/v1/auth/register` |
| Authentication | Not Required |

Purpose:

Registers a new user account.

---

## Login

| Property | Value |
|----------|-------|
| Method | POST |
| Endpoint | `/api/v1/auth/login` |
| Authentication | Not Required |

Purpose:

Authenticates a user and returns a JWT.

---

# 7. Review APIs

## Create Review

| Property | Value |
|----------|-------|
| Method | POST |
| Endpoint | `/api/v1/reviews` |
| Authentication | Required |

Purpose:

Uploads source code and creates a new AI review session.

---

## Get Review History

| Property | Value |
|----------|-------|
| Method | GET |
| Endpoint | `/api/v1/reviews` |
| Authentication | Required |

Purpose:

Returns all reviews belonging to the authenticated user.

---

## Get Review Details

| Property | Value |
|----------|-------|
| Method | GET |
| Endpoint | `/api/v1/reviews/{reviewId}` |
| Authentication | Required |

Purpose:

Returns details for a specific review.

---

## Delete Review

| Property | Value |
|----------|-------|
| Method | DELETE |
| Endpoint | `/api/v1/reviews/{reviewId}` |
| Authentication | Required |

Purpose:

Deletes an existing review.

---

# 8. Streaming API

Server-Sent Events (SSE) are used to stream review progress.

## Stream Review Progress

| Property | Value |
|----------|-------|
| Method | GET |
| Endpoint | `/api/v1/reviews/{reviewId}/stream` |
| Authentication | Required |

Purpose:

Streams AI-generated review events to the client in real time.

---

## Streaming Events

The backend may emit the following events.

| Event | Description |
|--------|-------------|
| REVIEW_STARTED | Review processing has begun |
| REVIEW_PROGRESS | Progress update |
| FINDING_DETECTED | New review finding generated |
| REVIEW_COMPLETED | Review completed successfully |
| REVIEW_FAILED | Review processing failed |

---

# 9. Request and Response Standards

## HTTP Headers

Protected APIs require:

```
Authorization: Bearer <JWT>
```

---

## Content Type

```
Content-Type: application/json
```

---

## File Upload

```
Content-Type: multipart/form-data
```

---

## Successful Responses

Use appropriate HTTP status codes.

Examples:

| Status | Meaning |
|--------|---------|
| 200 | Success |
| 201 | Resource Created |
| 204 | Resource Deleted |

---

# 10. Error Handling

Errors follow a standardized structure.

Fields include:

| Field | Description |
|--------|-------------|
| timestamp | Error timestamp |
| status | HTTP status |
| error | Short error description |
| message | Detailed error message |
| path | Request URI |
| correlationId | Request trace identifier |

Common HTTP status codes:

| Status | Meaning |
|--------|---------|
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Resource Not Found |
| 413 | File Too Large |
| 422 | Unsupported File |
| 500 | Internal Server Error |

---

# 11. Pagination Strategy

Collection APIs will support pagination.

Future parameters:

```
?page=0
&size=20
&sort=createdAt,desc
```

Pagination will follow Spring Data conventions.

---

# 12. Filtering and Searching

Future filtering capabilities include:

- Review status
- Severity
- Date range
- File name

Example:

```
GET /api/v1/reviews?status=COMPLETED
```

---

# 13. API Naming Conventions

The following naming conventions apply:

- Use plural resource names.
- Use lowercase URLs.
- Use nouns instead of verbs.
- Use path variables for resource identifiers.
- Maintain consistent endpoint naming.

---

# 14. Idempotency

| HTTP Method | Idempotent |
|-------------|------------|
| GET | Yes |
| POST | No |
| PUT | Yes |
| PATCH | Depends |
| DELETE | Yes |

Creating a review is intentionally **not idempotent**, as each request represents a new review session.

---

# 15. File Upload Constraints

Version 1 supports:

- Java source files
- UTF-8 encoding
- Configurable maximum upload size

Future versions may support ZIP uploads for project-level analysis.

---

# 16. Rate Limiting

Rate limiting is not part of Version 1.

Future versions will introduce limits for:

- Authentication endpoints
- Review creation
- File uploads
- AI provider usage

---

# 17. Future APIs

Future versions may expose APIs for:

- GitHub Pull Request integration
- Repository management
- Organization management
- Team collaboration
- AI provider administration
- Prompt management

---

# 18. API Governance

Every API introduced into the system must define:

- Purpose
- Resource ownership
- HTTP method selection
- Authentication requirements
- Request validation
- Response format
- Error responses
- Idempotency
- Versioning impact

This ensures consistency and maintainability as the API surface grows.