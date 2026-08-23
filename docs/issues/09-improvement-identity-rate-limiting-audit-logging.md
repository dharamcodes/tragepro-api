# [Improvement: Identity] Rate Limiting & Security Audit Event Logging

**Type**: Feature Improvement
**Module**: `identity`
**Labels**: `area:identity`, `security`, `rate-limiting`, `audit`
**Priority**: Medium

---

## 1. Current State & Limitations
Authentication endpoints (`/login`, `/signup`, `/reset-password`) are vulnerable to brute-force attacks and credential stuffing if not protected by intelligent rate limiting. Furthermore, there is no centralized audit trail of security-sensitive operations (password resets, role elevations, account deactivations).

---

## 2. Proposed Solution & Technical Design

1. **IP & User Rate Limiting (Token Bucket / Bucket4j + Redis)**:
   - Max 5 failed login attempts per minute per IP.
   - Max 3 password reset requests per hour per email.
   - Return HTTP 429 `Too Many Requests` with `Retry-After` header when limit is exceeded.
2. **Security Audit Event Logging**:
   - Publish `SecurityAuditEvent` via Spring Modulith event system on:
     - `LOGIN_SUCCESS`, `LOGIN_FAILURE`, `PASSWORD_CHANGED`, `ACCOUNT_LOCKED`, `MFA_ENABLED`.
   - Store events in `security_audit_logs` MongoDB collection with timestamp, IP address, user-agent, and failure reason.
3. **Account Lockout Policy**:
   - Temporarily lock account for 15 minutes after 5 consecutive failed login attempts.

---

## 3. Implementation Checklist

- [ ] Add Bucket4j / Redis token-bucket filter on `/api/v1/auth/**`.
- [ ] Create `SecurityAuditEntity` and `SecurityAuditEventListener`.
- [ ] Implement account lockout threshold in [`AuthenticationServiceImpl`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/identity/service/impl/AuthenticationServiceImpl.java).
- [ ] Add tests verifying rate limit triggers and audit record generation.
