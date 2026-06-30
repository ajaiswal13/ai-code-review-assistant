# ADR-002: Server-Sent Events for AI Streaming

## Status

Accepted

---

## Context

AI code reviews are long-running operations.

Users should receive incremental progress updates without repeatedly polling the backend.

---

## Decision

Server-Sent Events (SSE) will be used to stream review progress from the backend to the frontend.

---

## Alternatives Considered

### Server-Sent Events (SSE)

**Pros**

- Simple implementation.
- Native browser support.
- Uses standard HTTP.
- Automatic reconnection.
- Ideal for one-way streaming.

**Cons**

- Supports only server-to-client communication.

---

### WebSockets

**Pros**

- Full-duplex communication.
- Extremely low latency.
- Suitable for chat and collaborative applications.

**Cons**

- More complex infrastructure.
- Additional connection lifecycle management.
- Unnecessary for one-way AI streaming.

---

### Polling

**Pros**

- Simple implementation.

**Cons**

- High network overhead.
- Poor user experience.
- Delayed updates.

---

## Consequences

### Positive

- Efficient streaming.
- Lower implementation complexity.
- Better user experience.
- Well suited to AI token streaming.

### Negative

- If future features require bidirectional communication, WebSockets may be introduced alongside SSE.

---

## Future Considerations

Future collaborative editing features may justify introducing WebSockets while retaining SSE for AI streaming.