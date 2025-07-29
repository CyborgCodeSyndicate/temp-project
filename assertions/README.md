# Assertions Module (`assertions`)

> 💍 Part of the **One Ring To Rule Them All** test automation framework by Cyborg Code Syndicate

## Purpose

The `assertions` module is a core part of the Cyborg Code Syndicate validation engine. It provides a domain-agnostic, pluggable system for asserting conditions across various targets (API responses, DB records, UI states, etc.). The module is framework-independent and intended to be extended or reused across adapters.

---

## 🧠 Purpose

The goal of this module is to:
- Define a **strongly-typed validation language** (assertion types, targets, and contracts)
- Provide **functional validation execution logic**
- Enable **soft/hard assertion handling**
- Allow **pluggable assertions via registry**
- Support integration in fluent test DSLs (like `quest.requestAndValidate(...)`)

---

## 🧩 Core Concepts

### ✅ `Assertion`
Represents a single validation rule with:
- `target`: Enum (e.g., `RestAssertionTarget.BODY`)
- `key`: JSONPath/header key/etc.
- `type`: `AssertionType` (e.g., `IS`, `CONTAINS`, `GREATER_THAN`)
- `expected`: Expected value
- `soft`: Whether the assertion is soft (won’t fail test immediately)

### 📊 `AssertionResult<T>`
Encapsulates:
- Whether validation passed
- Actual vs Expected values
- Human-readable description
- Soft assertion indicator

### 🎯 `AssertionTarget<T extends Enum<T>>`
Marks the validation scope (body, header, db column, UI element...).

Used by consumers like `RestResponseValidatorImpl` to extract correct data for validation.

### 🧠 `AssertionType<T extends Enum<T>>`
Each assertion type declares:
- A symbolic identifier (e.g., `IS`, `NOT_NULL`)
- A compatible Java type (`String`, `Number`, `Collection`...)

Used by the engine to verify runtime compatibility.

### 🧾 `AssertionTypes`
Predefined built-in types like:
- `IS`, `NOT`, `GREATER_THAN`, `CONTAINS`, `IS_NULL`, `MATCHES_REGEX`, `EQUALS_IGNORE_CASE`...

Each one maps to a validator in `AssertionFunctions`.

---

## 🛠️ Execution Flow

### 🧪 `AssertionFunctions`
Pure static functional assertions:
- `equals(actual, expected)`
- `greaterThan(actual, expected)`
- `containsAll(actual, expected)`
- `matchesRegex(actual, expected)`
- `between(actual, expected)` (requires List of 2)

These functions throw errors for mismatches or misuse (e.g., trying `GREATER_THAN` on a String).

---

### 🧱 `AssertionRegistry`
Centralized registry of:
```java
Map<AssertionType<?>, BiPredicate<Object, Object>>
```

- All built-in assertions registered statically.
- Custom assertions can be added at runtime.

---

### 🧰 `AssertionUtil`
Executes validation:
```java
List<AssertionResult<T>> validate(Map<String, T> data, List<Assertion> assertions)
```

Internally:
- Validates assertion structure
- Resolves assertion type from registry
- Executes validator
- Builds result object

Fails early on:
- Missing keys in data map
- Incompatible types
- Unsupported assertion types

---

## ❗ Error Handling

### `InvalidAssertionException`
Thrown when an assertion is invalid:
- Null key
- Unsupported value
- Null `expected`, etc.

---

## 🔍 Example Usage (API Validator)

```java
Assertion.builder()
  .target(RestAssertionTarget.BODY)
  .key("user.name")
  .type(IS)
  .expected("Alice")
  .soft(true)
  .build();
```

And then validated via:

```java
List<AssertionResult<T>> results = AssertionUtil.validate(responseDataMap, assertions);
```

---

## 🧪 Extending Assertions

You can register custom validators:

```java
AssertionRegistry.registerCustomAssertion(MY_TYPE, (actual, expected) -> actual.toString().startsWith("x"));
```

---

## 🧬 Summary

| Component            | Responsibility                                     |
|---------------------|-----------------------------------------------------|
| `Assertion`          | Validation contract                                |
| `AssertionResult`    | Encapsulates outcome                               |
| `AssertionType`      | Categorizes logic                                  |
| `AssertionTarget`    | Defines validation scope (header, body, etc.)      |
| `AssertionFunctions` | Functional logic                                   |
| `AssertionRegistry`  | Maps type -> validator                             |
| `AssertionUtil`      | Executes assertion rules and returns results       |

---

© Cyborg Code Syndicate 💍👨💻
