/**
 * tragepro API - Bruno Test Suite Common Library
 * ================================================
 * Centralized helper functions for variable management,
 * assertions, test data generation, and shared test utilities.
 *
 * Usage: require('./common') is NOT needed in Bruno scripts.
 * Bruno automatically loads lib/common.js into the script context.
 * Call these functions directly in pre-request and post-response scripts.
 */

// ─────────────────────────────────────────────────────────────────────────────
// VARIABLE KEYS — single source of truth for all env/collection var names
// ─────────────────────────────────────────────────────────────────────────────
const VARS = {
  // Auth
  AUTH_TOKEN:         'authToken',
  RESET_TOKEN:        'resetToken',

  // Authentication module
  AUTH_USERNAME:      'authUserName',
  AUTH_EMAIL:         'authEmail',
  AUTH_PASSWORD:      'authPassword',
  AUTH_ROLE:          'authRole',

  // Account module
  ACCOUNT_IDENTIFIER: 'accountIdentifier',
  ACCOUNT_NAME:       'accountName',
  ACCOUNT_EMAIL:      'accountEmail',
  ACCOUNT_PHONE:      'accountPhoneNumber',

  // Candle module
  CANDLE_ID:          'candleId',
  SYMBOL_ID:          'symbolId',
  SYMBOL_NAME:        'symbolName',
};

// ─────────────────────────────────────────────────────────────────────────────
// RESET HELPERS — clear variables between test scenarios
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Resets all auth-related variables (call after logout / deactivation tests).
 */
const resetAuthVars = () => {
  bru.setVar(VARS.AUTH_TOKEN, '');
  bru.setVar(VARS.RESET_TOKEN, '');
  bru.setVar(VARS.AUTH_USERNAME, '');
  bru.setVar(VARS.AUTH_EMAIL, '');
  bru.setVar(VARS.AUTH_PASSWORD, '');
  bru.setVar(VARS.AUTH_ROLE, '');
};

/**
 * Resets all account-related variables (call after delete / deactivation tests).
 */
const resetAccountVars = () => {
  bru.setVar(VARS.ACCOUNT_IDENTIFIER, '');
  bru.setVar(VARS.ACCOUNT_NAME, '');
  bru.setVar(VARS.ACCOUNT_EMAIL, '');
  bru.setVar(VARS.ACCOUNT_PHONE, '');
};

/**
 * Resets all candle data variables (call after delete tests).
 */
const resetCandleVars = () => {
  bru.setVar(VARS.CANDLE_ID, '');
  bru.setVar(VARS.SYMBOL_ID, '');
  bru.setVar(VARS.SYMBOL_NAME, '');
};

/**
 * Full reset — clears ALL test variables.
 * Useful as a tear-down step at the end of a full run.
 */
const resetAllVars = () => {
  resetAuthVars();
  resetAccountVars();
  resetCandleVars();
};

// ─────────────────────────────────────────────────────────────────────────────
// SET HELPERS — populate variables from response body
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Saves the JWT token from a login response.
 * @param {object} body - res.body from login
 */
const setAuthToken = (body) => {
  if (body && body.token) {
    bru.setVar(VARS.AUTH_TOKEN, body.token);
    bru.setEnvVar(VARS.AUTH_TOKEN, body.token);
  }
};

/**
 * Saves authentication user details from signup / get-user response.
 * @param {object} body - res.body from signup or get-user
 */
const setAuthUserVars = (body) => {
  if (!body) return;
  if (body.userName) bru.setVar(VARS.AUTH_USERNAME, body.userName);
  if (body.role)     bru.setVar(VARS.AUTH_ROLE, body.role);
};

/**
 * Saves account details from create-account or get-account response.
 * @param {object} body - res.body from account endpoints
 */
const setAccountVars = (body) => {
  if (!body) return;
  if (body.identifier)  bru.setVar(VARS.ACCOUNT_IDENTIFIER, body.identifier);
  if (body.name)        bru.setVar(VARS.ACCOUNT_NAME, body.name);
  if (body.email)       bru.setVar(VARS.ACCOUNT_EMAIL, body.email);
  if (body.phoneNumber) bru.setVar(VARS.ACCOUNT_PHONE, body.phoneNumber);
};

/**
 * Saves candle record details from create / get-by-id response (full CandleResponse).
 * @param {object} body - res.body from POST /candle or GET /candle/{id}
 */
const setCandleVars = (body) => {
  if (!body) return;
  if (body.id) bru.setVar(VARS.CANDLE_ID, body.id);
  // Full CandleResponse — nested symbolData
  if (body.symbolData) {
    if (body.symbolData.id)   bru.setVar(VARS.SYMBOL_ID, body.symbolData.id);
    if (body.symbolData.name) bru.setVar(VARS.SYMBOL_NAME, body.symbolData.name);
  }
  // CandleSummaryResponse — flat fields
  if (body.symbolId)   bru.setVar(VARS.SYMBOL_ID, body.symbolId);
  if (body.symbolName) bru.setVar(VARS.SYMBOL_NAME, body.symbolName);
};

/**
 * Saves candle vars from the first element of a summary list response.
 * @param {Array} body - res.body from GET /candle (page content) or /symbols/latest
 */
const setCandleVarsFromList = (body) => {
  if (!body || !Array.isArray(body) || body.length === 0) return;
  setCandleVars(body[0]);
};

/**
 * Saves candle vars from the content array of a paginated summary response.
 * @param {object} body - res.body from paginated endpoints (has .content array)
 */
const setCandleVarsFromPage = (body) => {
  if (!body || !body.content || body.content.length === 0) return;
  setCandleVars(body.content[0]);
};

// ─────────────────────────────────────────────────────────────────────────────
// ASSERTION HELPERS — reusable test assertions
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Asserts the HTTP response status matches the expected code.
 * @param {number} expected - expected HTTP status code
 */
const assertStatus = (expected) => {
  test(`Status code is ${expected}`, () => {
    expect(res.status).to.equal(expected);
  });
};

/**
 * Asserts that the response Content-Type is application/json.
 */
const assertJsonContentType = () => {
  test('Response Content-Type is application/json', () => {
    expect(res.headers['content-type']).to.include('application/json');
  });
};

/**
 * Asserts that a field exists and is not null/undefined/empty.
 * @param {*}      value     - value to check
 * @param {string} fieldName - human-readable name for test label
 */
const assertFieldPresent = (value, fieldName) => {
  test(`Field "${fieldName}" is present`, () => {
    expect(value).to.not.be.undefined;
    expect(value).to.not.be.null;
    expect(value).to.not.equal('');
  });
};

/**
 * Asserts that the response body has a specific top-level key.
 * @param {string} key - key to check in res.body
 */
const assertBodyHasKey = (key) => {
  test(`Response body has key "${key}"`, () => {
    expect(res.body).to.have.property(key);
  });
};

/**
 * Asserts a pageable response has the standard Spring Page structure.
 */
const assertPageStructure = () => {
  test('Response has page structure (content, totalElements, totalPages)', () => {
    expect(res.body).to.have.property('content');
    expect(res.body).to.have.property('totalElements');
    expect(res.body).to.have.property('totalPages');
    expect(res.body.content).to.be.an('array');
  });
};

/**
 * Runs common success checks: status 200 + JSON content type.
 */
const assertOk = () => {
  assertStatus(200);
  assertJsonContentType();
};

/**
 * Runs common accepted checks: status 202 (no body expected).
 */
const assertAccepted = () => {
  assertStatus(202);
};

/**
 * Asserts error response follows the AppErrorDto shape.
 * @param {number} expectedStatus - expected HTTP status code
 */
const assertErrorResponse = (expectedStatus) => {
  assertStatus(expectedStatus);
  test('Error response has "message" field', () => {
    expect(res.body).to.have.property('message');
  });
};

// ─────────────────────────────────────────────────────────────────────────────
// TEST DATA GENERATORS — deterministic fixture values
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Returns a unique-ish suffix string using current timestamp mod to keep it short.
 * Use this to create data that doesn't collide across runs.
 */
const uniqueSuffix = () => String(Date.now()).slice(-6);

/** Default SUPER_USER signup fixture. */
const defaultSignupBody = () => ({
  email: `test.user.${uniqueSuffix()}@tragepro.com`,
  userName: `testuser${uniqueSuffix()}`,
  password: 'Password@123',
  role: 'SUPER_USER',
  isActive: true,
});

/** Default login fixture — must match an existing user in the environment. */
const defaultLoginBody = () => ({
  userName: bru.getVar(VARS.AUTH_USERNAME) || 'admin01',
  password: 'Password@123',
});

/** Default account detail fixture. */
const defaultAccountBody = () => ({
  name: `Test Account ${uniqueSuffix()}`,
  email: `account.${uniqueSuffix()}@tragepro.com`,
  phoneNumber: 9876543210,
  isActive: true,
});

/** Default candle data fixture. */
const defaultCandleBody = () => ({
  symbolData: {
    id: `SYM${uniqueSuffix()}`,
    name: `TEST_SYMBOL_${uniqueSuffix()}`,
  },
  candle: {
    timestamp: Date.now(),
    open: 100.50,
    high: 115.75,
    low: 98.20,
    close: 110.30,
    volume: 500000.0,
  },
});