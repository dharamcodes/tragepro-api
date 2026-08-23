# [Improvement: Identity] Refresh Token Rotation, Redis Token Blacklist & MFA / 2FA Support

**Type**: Feature Improvement
**Module**: `identity`
**Labels**: `area:identity`, `security`, `improvement`, `jwt`
**Priority**: High

---

## 1. Current State & Limitations
The current `identity` module uses standard JWT access tokens with a long expiration time (7200 minutes). If an access token is compromised, there is no mechanism to invalidate or blacklist it before expiry. Additionally, there is no Multi-Factor Authentication (MFA / 2FA) protection for user accounts.

---

## 2. Proposed Solution & Technical Design

1. **Short-Lived Access Tokens + Refresh Token Rotation**:
   - Access token lifespan: 15 minutes.
   - Refresh token lifespan: 7 days (stored securely in HttpOnly cookie or secure storage).
   - Rotating refresh tokens: Every time a refresh token is used, it is invalidated and replaced with a new one. Reuse detection revokes all active sessions for the user.
2. **Redis Token Blacklist**:
   - On `/api/v1/auth/logout` or password change, write the JWT ID (`jti`) with remaining TTL to Redis.
   - `JWTAuthFilter` checks the blacklist on each request with < 1ms Redis lookup.
3. **Time-Based One-Time Password (TOTP) MFA**:
   - `/api/v1/auth/mfa/setup`: Generates QR code and secret for Google Authenticator / Authy.
   - `/api/v1/auth/mfa/verify`: Validates 6-digit TOTP code during login.

---

## 3. Implementation Checklist

- [ ] Add `RefreshTokenEntity` and repository in `identity.core`.
- [ ] Add Redis cache configuration for JWT revocation blacklist.
- [ ] Implement `TOTPHelper` (RFC 6238 compliant).
- [ ] Add `/api/v1/auth/refresh` and MFA endpoints to [`AuthenticationController`](file:///Users/dharam.dev/devspace/tragepro-api/src/main/java/com/tragepro/api/identity/web/AuthenticationController.java).
- [ ] Unit and integration tests (>= 95% line coverage).
