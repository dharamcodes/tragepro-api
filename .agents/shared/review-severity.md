# Code Review Severity Guidelines

This document defines the standard severity classifications used by **Loki** and **Odin** during code and architecture reviews.

---

## 🚦 Severity Matrix

| Severity | Description | Action Required | Blocks Merge? |
| :--- | :--- | :--- | :--- |
| 🔴 **`BLOCKER`** | Severe security flaw, data loss/corruption risk, system outage hazard, or fundamental architectural violation. | Immediate remediation required. | **YES** |
| 🟠 **`CRITICAL`** | Severe correctness bug, broken acceptance criteria, missing error handling on critical paths, or unhandled concurrency hazard. | Must be fixed before review approval. | **YES** |
| 🟡 **`MAJOR`** | Significant design defect, missing edge-case coverage, Modulith boundary leak, missing integration/Bruno test, or performance hazard. | Must be resolved before review approval. | **YES** |
| 🔵 **`MINOR`** | Non-critical improvement, suboptimal naming, minor duplicate code, missing documentation, or test refinement. | Thor should fix if straightforward, or document rationale. | **NO** (Discretionary) |
| ⚪ **`SUGGESTION`** | Stylistic idea, non-essential future optimization, or alternative idiom. | Optional; does not require code change. | **NO** |

---

## 🔍 Detailed Severity Definitions & Examples

### 🔴 1. BLOCKER
A catastrophic issue that poses immediate risk to system stability, security, or architecture.

**Examples in `tragepro-api`**:
- Logging passwords, private keys, broker API secrets, or raw JWT tokens.
- Hardcoding credentials in production classes or committing real secrets to Bruno test files.
- SQL/NoSQL injection vulnerability in MongoDB queries.
- Breaking Spring Modulith encapsulation by creating circular module dependencies or illegally accessing private module internals.
- Unprotected financial execution endpoint allowing unauthorized trade execution.

---

### 🟠 2. CRITICAL
A severe correctness or reliability flaw that breaks specified functionality or poses production failure risks.

**Examples in `tragepro-api`**:
- An acceptance criterion from `docs/issues/*.md` is completely unimplemented or behaves incorrectly.
- Missing input validation on critical API fields (e.g., negative trade amounts, null symbols).
- Non-deterministic logic placed inside a Temporal Workflow definition (e.g., calling `UUID.randomUUID()` or network I/O).
- Missing transaction boundaries causing partial database writes on multi-document updates.
- Race conditions in shared state (e.g., mutating non-thread-safe collections in singletons).

---

### 🟡 3. MAJOR
An important engineering, design, or testing defect that impacts system maintainability, resilience, or test integrity.

**Examples in `tragepro-api`**:
- Missing Bruno API E2E tests for a new or modified REST endpoint.
- JaCoCo line coverage falling below the mandatory $95\%$ threshold.
- Missing timeout or circuit breaker on an external broker client or market data feed.
- MongoDB query performing full collection scans due to missing `@Indexed` or compound index.
- Returning `@Document` entities directly in REST controller responses instead of MapStruct DTO records.
- Controller containing business logic instead of delegating to application services.

---

### 🔵 4. MINOR
A non-critical quality improvement that enhances clarity or maintainability without affecting functionality.

**Examples in `tragepro-api`**:
- Ambiguous method or variable names.
- Minor code duplication across helper methods.
- Missing Javadoc on public adapter interface methods (`@NamedInterface("adapter")`).
- Suboptimal assertion messages in unit test methods.

---

### ⚪ 5. SUGGESTION
An educational note, idiomatic suggestion, or future idea.

**Examples in `tragepro-api`**:
- Using Java 25 pattern matching switch instead of chained `if-else` blocks.
- Suggesting a more expressive AssertJ assertion method.
- Recommending a potential performance optimization for future high-volume scaling.

---

## 📝 Review Comment Formatting Standard

Reviewers (Loki and Odin) must format review findings using the standard tag format:

```markdown
### [SEVERITY] Brief Summary of Issue
**Location**: `path/to/File.java#L45-L52`
**Problem**: Clear explanation of the flaw, why it matters, and its risk.
**Recommendation**: Concrete code snippet or actionable step to resolve.
```
