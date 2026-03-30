# Test Cases

This document provides the complete test documentation for the **Java Age Calculator** application, including the test strategy, a comprehensive test case matrix, detailed scenario documentation, and edge case coverage. Every test case listed here maps directly to the validation and calculation logic implemented in the application.

The Age Calculator is a **Java console application** that accepts a Date of Birth in `DD/MM/YYYY` format and outputs the exact age in years, months, and days using the `java.time` API (`LocalDate`, `Period`, and `DateTimeFormatter`).

**Test Approach:** All testing is performed through **manual test verification** — the tester runs the application interactively via the console and verifies the output against the expected results documented below. No JUnit or other automated test framework is used.

---

## Table of Contents

- [Test Strategy Overview](#test-strategy-overview)
- [Test Case Matrix](#test-case-matrix)
- [Normal Date Test Scenarios](#normal-date-test-scenarios)
- [Leap Year Test Scenarios](#leap-year-test-scenarios)
- [Invalid Date Test Scenarios](#invalid-date-test-scenarios)
- [Future Date Test Scenarios](#future-date-test-scenarios)
- [Wrong Format Test Scenarios](#wrong-format-test-scenarios)
- [Edge Cases](#edge-cases)
- [How to Execute Tests](#how-to-execute-tests)
- [Related Documentation](#related-documentation)

### Quick Navigation

- [Usage Guide](../getting-started/usage.md) — Error message details and usage examples
- [AgeCalculator API](../api-reference/age-calculator.md) — Main class method documentation
- [DateValidator API](../api-reference/date-validator.md) — Validation logic details
- [Architecture Overview](../architecture/overview.md) — System design and validation flowchart
- [README](../../README.md) — Project overview

---

## Test Strategy Overview

### Test Type

**Manual console testing** — the tester compiles and runs the application, enters input interactively via the console, and compares the actual output against the expected output documented in this file.

### Test Environment

- **JDK:** 8 or higher (the `java.time` API was introduced in Java 8)
- **Compile:** `javac src/*.java`
- **Run:** `java -cp src AgeCalculator`

### Test Scope

All tests cover the following areas of the application:

1. **Input validation paths** — format validation, calendar validity, and temporal validity
2. **Core age calculation logic** — correctness of `Period.between()` results for various date combinations
3. **Output formatting** — verification that the result matches the exact format `Your age is X years, Y months, and Z days.`
4. **Error handling** — verification that meaningful, user-friendly error messages are displayed for every invalid input (no raw Java stack traces)

### Pass and Fail Criteria

| Criteria Type | Definition |
|---------------|------------|
| **Pass** | The application output matches the expected output exactly (for valid inputs) or displays the correct error message (for invalid inputs) |
| **Fail** | The application crashes, produces incorrect output, displays a wrong or missing error message, shows a raw Java stack trace, or accepts input that should be rejected |

### Validation Pipeline Under Test

The application processes every input through a five-stage validation and calculation pipeline. Each stage is tested by one or more test cases in this document:

1. **Format Validation** — Input must match the `DD/MM/YYYY` pattern. The `DateValidator.parseDate()` method uses `DateTimeFormatter.ofPattern("dd/MM/uuuu")` with `ResolverStyle.STRICT` to enforce the expected format. Inputs that fail this stage throw a `DateTimeParseException`.
2. **Calendar Validation** — The date must exist on the Gregorian calendar. For example, `31/02/2020` is rejected because February never has 31 days. The strict resolver in `DateTimeFormatter` catches these invalid calendar dates automatically.
3. **Temporal Validation** — The date must not be in the future. `DateValidator.isFutureDate()` compares the parsed `LocalDate` with `LocalDate.now()` and rejects dates that are after today.
4. **Age Calculation** — `Period.between(birthDate, LocalDate.now())` computes the exact difference in years, months, and days.
5. **Output Formatting** — The result is formatted and displayed as `Your age is X years, Y months, and Z days.`

> See the [Architecture Overview](../architecture/overview.md) for the complete validation flowchart.

### Exception Handling Under Test

The application wraps all logic in `try-catch` blocks to ensure that no raw Java stack trace is ever displayed to the user. The following exception types are tested:

| Exception | Trigger | Expected Behavior |
|-----------|---------|-------------------|
| `DateTimeParseException` | Input does not match `DD/MM/YYYY` format or represents an invalid calendar date | Caught in `try-catch`; displays `Error: Invalid date format. Please use DD/MM/YYYY.` or `Error: Invalid calendar date. The date does not exist on the calendar.` |
| `IllegalArgumentException` | Input is `null` or empty | Caught in `try-catch`; displays `Error: Input cannot be null or empty.` |

> **Note on future dates:** Future dates are **not** handled via an exception. The `DateValidator.isFutureDate()` method returns a `boolean` value (`true` if the date is in the future), and the `main()` method checks this return value with an `if` statement — displaying `Error: Date of Birth cannot be in the future.` without throwing or catching an exception. See the [Architecture Overview](../architecture/overview.md) for the exception propagation chain.

Each error path documented in this file must display a **meaningful, user-friendly error message** rather than a raw exception.

> **Note:** Since this is a console application with no automated test framework, all testing is performed by manual execution and visual verification. See [How to Execute Tests](#how-to-execute-tests) for step-by-step instructions.

---

## Test Case Matrix

The following table summarizes all test cases for the Age Calculator application. Each test case is documented in detail in the sections below.

| Test ID | Category | Scenario | Input | Expected Output | Status |
|---------|----------|----------|-------|-----------------|--------|
| TC-001 | ✅ Normal | Standard date of birth | `15/08/1998` | `Your age is X years, Y months, and Z days.` | Pass |
| TC-002 | ✅ Normal | Another standard date | `01/01/2000` | `Your age is X years, Y months, and Z days.` | Pass |
| TC-003 | ✅ Normal | Recent date of birth | `25/12/2020` | `Your age is X years, Y months, and Z days.` | Pass |
| TC-004 | ✅ Leap Year | Leap year DOB (Feb 29) | `29/02/2000` | `Your age is X years, Y months, and Z days.` | Pass |
| TC-005 | ❌ Leap Year | Non-leap year Feb 29 (invalid) | `29/02/2001` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-006 | ❌ Invalid Date | February 31 | `31/02/2020` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-007 | ❌ Invalid Date | February 30 | `30/02/2020` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-008 | ❌ Invalid Date | Century non-leap year Feb 29 | `29/02/1900` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-009 | ❌ Invalid Date | Month 13 | `15/13/1998` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-010 | ❌ Invalid Date | Day 32 | `32/01/1998` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-011 | ❌ Future Date | Date in the future | `25/12/2030` | `Error: Date of Birth cannot be in the future.` | Fail (expected error) |
| TC-012 | ❌ Future Date | Far future date | `01/01/2099` | `Error: Date of Birth cannot be in the future.` | Fail (expected error) |
| TC-013 | ❌ Wrong Format | Non-date text | `hello` | `Error: Invalid date format. Please use DD/MM/YYYY.` | Fail (expected error) |
| TC-014 | ❌ Wrong Format | Wrong separator | `15-08-1998` | `Error: Invalid date format. Please use DD/MM/YYYY.` | Fail (expected error) |
| TC-015 | ❌ Wrong Format | Wrong order (MM/DD/YYYY) | `08/15/1998` | `Error: Invalid calendar date. The date does not exist on the calendar.` | Fail (expected error) |
| TC-016 | ❌ Wrong Format | Empty input | *(empty string)* | `Error: Invalid date format. Please use DD/MM/YYYY.` | Fail (expected error) |
| TC-017 | ✅ Edge Case | Today's date (newborn) | *(today's date)* | `Your age is 0 years, 0 months, and 0 days.` | Pass |
| TC-018 | ✅ Edge Case | Yesterday's date | *(yesterday's date)* | `Your age is 0 years, 0 months, and 1 days.` | Pass |
| TC-019 | ✅ Edge Case | Very old date | `01/01/1900` | `Your age is X years, Y months, and Z days.` | Pass |
| TC-020 | ✅ Edge Case | Same-day birthday | *(today's month/day, past year)* | `Your age is X years, 0 months, and 0 days.` | Pass |
| TC-021 | ✅ Edge Case | End of month boundary | `31/01/1998` | `Your age is X years, Y months, and Z days.` | Pass |

> **Note:** Values shown as `X years, Y months, and Z days` vary based on the current system date at the time of test execution. Testers should calculate the expected values using `Period.between(birthDate, LocalDate.now())` based on the actual test execution date.

> **Note on TC-015:** The input `08/15/1998` is interpreted as day `08`, month `15`, year `1998`. Since month `15` does not exist on the calendar, the application treats this as an invalid calendar date rather than a format error.

---

## Normal Date Test Scenarios

These test cases verify the **happy path** — valid dates of birth that should produce a correctly calculated age.

### Test Case TC-001: Standard Date of Birth

| Field | Value |
|-------|-------|
| **Test ID** | TC-001 |
| **Scenario** | User enters a valid, standard date of birth |
| **Category** | ✅ Normal |
| **Preconditions** | Application is compiled and running; current date is after 15/08/1998 |
| **Input** | `15/08/1998` |

**Steps:**

1. Run the application: `java -cp src AgeCalculator`
2. When prompted `Enter your Date of Birth (DD/MM/YYYY):`, enter `15/08/1998`
3. Press Enter

**Expected Output:**

```
Your age is 27 years, 6 months, and 15 days.
```

*(Exact values depend on the current date. The example above assumes a test execution date of approximately March 2026.)*

**Pass Criteria:** Output matches the format `Your age is X years, Y months, and Z days.` with correct values based on `Period.between(LocalDate.of(1998, 8, 15), LocalDate.now())`.

**Fail Criteria:** Application crashes, output format differs from the expected pattern, or age values are mathematically incorrect.

**Notes:** This is the **primary test case from the user specification**. The exact year, month, and day values will change based on when the test is executed. To verify correctness, manually compute the expected `Period` between August 15, 1998 and the current date.

---

### Test Case TC-002: Another Standard Date

| Field | Value |
|-------|-------|
| **Test ID** | TC-002 |
| **Scenario** | User enters a different valid date to verify calculation consistency |
| **Category** | ✅ Normal |
| **Preconditions** | Application is compiled and running; current date is after 01/01/2000 |
| **Input** | `01/01/2000` |

**Expected Output:**

```
Your age is X years, Y months, and Z days.
```

**Pass Criteria:** Output format matches exactly; age values are correct for January 1, 2000 relative to the current date.

**Fail Criteria:** Application crashes, output format differs, or age values are incorrect.

---

### Test Case TC-003: Recent Date of Birth

| Field | Value |
|-------|-------|
| **Test ID** | TC-003 |
| **Scenario** | User enters a recent but past date of birth |
| **Category** | ✅ Normal |
| **Preconditions** | Application is compiled and running; current date is after 25/12/2020 |
| **Input** | `25/12/2020` |

**Expected Output:**

```
Your age is X years, Y months, and Z days.
```

**Pass Criteria:** Correctly computes a small age (a few years); output format matches the expected pattern.

**Fail Criteria:** Application crashes, incorrectly rejects the date, or produces incorrect age values.

---

## Leap Year Test Scenarios

These test cases verify correct handling of leap year dates. The leap year rules are critical for validating February 29 dates:

- A year is a **leap year** if it is divisible by **4**
- **Exception:** Years divisible by **100** are **NOT** leap years
- **Exception to the exception:** Years divisible by **400** **ARE** leap years

**Leap Year Examples:**

| Year | Divisible by 4? | Divisible by 100? | Divisible by 400? | Leap Year? |
|------|-----------------|--------------------|--------------------|------------|
| 2000 | ✅ | ✅ | ✅ | ✅ Yes |
| 1900 | ✅ | ✅ | ❌ | ❌ No |
| 2024 | ✅ | ❌ | — | ✅ Yes |
| 2023 | ❌ | — | — | ❌ No |

The `java.time.LocalDate` API handles all leap year logic correctly. The `DateTimeFormatter` with `ResolverStyle.STRICT` automatically rejects February 29 in non-leap years during parsing.

### Test Case TC-004: Leap Year DOB (February 29)

| Field | Value |
|-------|-------|
| **Test ID** | TC-004 |
| **Scenario** | User enters a Date of Birth on February 29 of a leap year |
| **Category** | ✅ Leap Year |
| **Preconditions** | Application is compiled and running; current date is after 29/02/2000 |
| **Input** | `29/02/2000` |

**Expected Output:**

```
Your age is X years, Y months, and Z days.
```

**Pass Criteria:** The date `29/02/2000` is accepted as valid (2000 is a leap year, divisible by 400). Age is calculated correctly using `Period.between()`, which handles the leap year birthday accurately even when the current year is not a leap year.

**Fail Criteria:** Application rejects the date as invalid, crashes, or calculates age incorrectly.

**Notes:** This is one of the **5 user-specified test cases**. The `java.time` API handles February 29 correctly in leap years. When calculating age from a February 29 birthday on a non-leap year, `Period.between()` computes the period accurately without requiring special-case logic.

---

### Test Case TC-005: Non-Leap Year February 29 (Invalid)

| Field | Value |
|-------|-------|
| **Test ID** | TC-005 |
| **Scenario** | User enters February 29 of a non-leap year |
| **Category** | ❌ Leap Year (invalid input) |
| **Preconditions** | Application is compiled and running |
| **Input** | `29/02/2001` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application correctly rejects the date and displays a meaningful error message. The year 2001 is not a leap year (not divisible by 4), so February 29 does not exist.

**Fail Criteria:** Application accepts the date, crashes, or displays a raw Java stack trace.

**Notes:** This test case is placed in the Leap Year section for context. The `DateTimeFormatter` with `ResolverStyle.STRICT` detects that February 29, 2001 does not exist and throws a `DateTimeParseException`, which is caught in the `try-catch` block and displayed as a user-friendly error.

---

## Invalid Date Test Scenarios

These test cases verify that the application rejects dates that are syntactically in `DD/MM/YYYY` format but do not exist on the Gregorian calendar. Examples include February 31 (no month has 31 days in February), June 31 (June has only 30 days), and February 29 in non-leap years.

The `DateValidator.parseDate()` method uses `DateTimeFormatter` with `ResolverStyle.STRICT` to reject these dates, throwing a `DateTimeParseException` that is caught in the `try-catch` block.

> See [DateValidator API Reference](../api-reference/date-validator.md) for full details on the validation rules and exception handling.

### Test Case TC-006: February 31 (Impossible Date)

| Field | Value |
|-------|-------|
| **Test ID** | TC-006 |
| **Scenario** | User enters February 31, which never exists on any calendar |
| **Category** | ❌ Invalid Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `31/02/2020` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application rejects the date and displays a meaningful error message. No age calculation is performed.

**Fail Criteria:** Application accepts the date, crashes, or shows a raw Java stack trace instead of a user-friendly message.

**Notes:** This is one of the **5 user-specified test cases**. The `DateTimeFormatter` with strict parsing rejects this input because February never has 31 days in any year. The resulting `DateTimeParseException` is caught in the `try-catch` block and converted to the error message shown above.

---

### Test Case TC-007: February 30 (Impossible Date)

| Field | Value |
|-------|-------|
| **Test ID** | TC-007 |
| **Scenario** | User enters February 30 |
| **Category** | ❌ Invalid Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `30/02/2020` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application rejects the date with the same error message as TC-006. February has at most 29 days (in leap years) and 28 days in common years.

**Fail Criteria:** Application accepts the date or crashes.

---

### Test Case TC-008: Century Non-Leap Year February 29

| Field | Value |
|-------|-------|
| **Test ID** | TC-008 |
| **Scenario** | User enters February 29 of a century year that is **not** a leap year (divisible by 100 but not by 400) |
| **Category** | ❌ Invalid Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `29/02/1900` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application correctly identifies that 1900 is **not** a leap year — although it is divisible by 100, it is not divisible by 400 — and rejects February 29 with a meaningful error message. This validates the century rule of the Gregorian calendar.

**Fail Criteria:** Application accepts the date, crashes, or displays a raw Java stack trace.

**Notes:** This test case complements TC-005 (`29/02/2001`, a simple non-leap year not divisible by 4) by exercising the **century exception rule**. Under the Gregorian calendar, a year is a leap year if divisible by 4, **except** for century years (divisible by 100), which must also be divisible by 400. For example, 2000 is a leap year (÷400) but 1900 is not (÷100 but not ÷400). The `DateTimeFormatter` with `ResolverStyle.STRICT` correctly enforces this rule.

---

### Test Case TC-009: Month 13 (Invalid Month)

| Field | Value |
|-------|-------|
| **Test ID** | TC-009 |
| **Scenario** | User enters a month value of 13 |
| **Category** | ❌ Invalid Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `15/13/1998` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application rejects the input because month values must be in the range 01–12.

**Fail Criteria:** Application accepts the date, crashes, or displays an incorrect error message.

---

### Test Case TC-010: Day 32 (Invalid Day)

| Field | Value |
|-------|-------|
| **Test ID** | TC-010 |
| **Scenario** | User enters a day value of 32 |
| **Category** | ❌ Invalid Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `32/01/1998` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application rejects the input because no month has more than 31 days. The strict resolver in `DateTimeFormatter` catches this automatically.

**Fail Criteria:** Application accepts the date, crashes, or displays an incorrect error message.

---

## Future Date Test Scenarios

These test cases verify that the application rejects dates that are in the future. A Date of Birth cannot be after the current system date.

The `DateValidator.isFutureDate()` method compares the parsed `LocalDate` with `LocalDate.now()` and returns `true` if the date is after today. When a future date is detected, the application displays an error message instead of attempting to calculate a negative age.

### Test Case TC-011: Future Date

| Field | Value |
|-------|-------|
| **Test ID** | TC-011 |
| **Scenario** | User enters a date that is in the future |
| **Category** | ❌ Future Date |
| **Preconditions** | Application is compiled and running; the input date is after the current system date |
| **Input** | `25/12/2030` *(or any date after today)* |

**Expected Output:**

```
Error: Date of Birth cannot be in the future.
```

**Pass Criteria:** Application correctly identifies the date as a future date and displays the error message. The application does **NOT** proceed to age calculation and does **NOT** display a negative age.

**Fail Criteria:** Application calculates a negative age, crashes, accepts the future date without error, or displays a raw Java stack trace.

**Notes:** This is one of the **5 user-specified test cases**. The validation uses `date.isAfter(LocalDate.now())` to detect future dates. Testers should use a date that is guaranteed to be in the future at the time of test execution.

---

### Test Case TC-012: Far Future Date

| Field | Value |
|-------|-------|
| **Test ID** | TC-012 |
| **Scenario** | User enters a date far in the future |
| **Category** | ❌ Future Date |
| **Preconditions** | Application is compiled and running |
| **Input** | `01/01/2099` |

**Expected Output:**

```
Error: Date of Birth cannot be in the future.
```

**Pass Criteria:** Same behavior as TC-011 — future dates are rejected regardless of how far in the future they are. The `isFutureDate()` check does not differentiate between near and distant future dates.

**Fail Criteria:** Application accepts the date, crashes, or behaves differently for far-future dates compared to near-future dates.

---

## Wrong Format Test Scenarios

These test cases verify that the application rejects input that does not conform to the expected `DD/MM/YYYY` format.

The application expects input **exactly** in `DD/MM/YYYY` format with forward slashes (`/`) as separators. The `DateValidator.parseDate()` method uses `DateTimeFormatter.ofPattern("dd/MM/uuuu")` with `ResolverStyle.STRICT`, which strictly validates the format. Any input that does not match this pattern throws a `DateTimeParseException`, caught in the `try-catch` block.

### Test Case TC-013: Non-Date Text Input

| Field | Value |
|-------|-------|
| **Test ID** | TC-013 |
| **Scenario** | User enters text that is not a date at all |
| **Category** | ❌ Wrong Format |
| **Preconditions** | Application is compiled and running |
| **Input** | `hello` |

**Expected Output:**

```
Error: Invalid date format. Please use DD/MM/YYYY.
```

**Pass Criteria:** Application rejects the input and displays the format error message. No age calculation is attempted.

**Fail Criteria:** Application crashes, displays a raw `DateTimeParseException` stack trace, or attempts to process the input.

**Notes:** This is one of the **5 user-specified test cases**. The string `hello` cannot be parsed by `DateTimeFormatter` with the `dd/MM/uuuu` pattern. The resulting `DateTimeParseException` is caught and a clear format instruction is displayed.

---

### Test Case TC-014: Wrong Separator (Hyphens)

| Field | Value |
|-------|-------|
| **Test ID** | TC-014 |
| **Scenario** | User enters a date with hyphens instead of forward slashes |
| **Category** | ❌ Wrong Format |
| **Preconditions** | Application is compiled and running |
| **Input** | `15-08-1998` |

**Expected Output:**

```
Error: Invalid date format. Please use DD/MM/YYYY.
```

**Pass Criteria:** Application rejects the input because the `DateTimeFormatter` pattern expects forward slashes (`/`), not hyphens (`-`).

**Fail Criteria:** Application accepts the input or crashes.

**Notes:** This is a common mistake for users who are accustomed to the ISO 8601 format (`YYYY-MM-DD`). The application only accepts forward slashes as separators.

---

### Test Case TC-015: Wrong Date Order (MM/DD/YYYY)

| Field | Value |
|-------|-------|
| **Test ID** | TC-015 |
| **Scenario** | User enters a date in American MM/DD/YYYY format instead of DD/MM/YYYY |
| **Category** | ❌ Wrong Format |
| **Preconditions** | Application is compiled and running |
| **Input** | `08/15/1998` |

**Expected Output:**

```
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Pass Criteria:** Application rejects the input. The parser interprets `08/15/1998` as day `08`, month `15`, year `1998`. Since month `15` does not exist on the calendar (valid range is 01–12), the strict resolver rejects the date.

**Fail Criteria:** Application accepts the input or crashes.

**Notes:** This test case demonstrates a subtle issue — the error message refers to an invalid calendar date rather than an invalid format because the input structurally matches `DD/MM/YYYY` (two digits, slash, two digits, slash, four digits) but contains an impossible month value. See the [Usage Guide](../getting-started/usage.md) for tips on correct date order.

---

### Test Case TC-016: Empty Input

| Field | Value |
|-------|-------|
| **Test ID** | TC-016 |
| **Scenario** | User presses Enter without typing anything |
| **Category** | ❌ Wrong Format |
| **Preconditions** | Application is compiled and running |
| **Input** | *(empty string)* |

**Expected Output:**

```
Error: Invalid date format. Please use DD/MM/YYYY.
```

**Pass Criteria:** Application handles empty input gracefully without crashing. An appropriate error message is displayed directing the user to use the correct format.

**Fail Criteria:** Application crashes, throws an unhandled `NullPointerException`, or hangs indefinitely.

---

## Edge Cases

The following test cases cover boundary conditions and unusual but valid inputs that verify the robustness of the age calculation logic. These scenarios test the limits of the `Period.between()` calculation and ensure the application handles extreme values correctly.

### Test Case TC-017: Today's Date (Newborn / Age Zero)

| Field | Value |
|-------|-------|
| **Test ID** | TC-017 |
| **Scenario** | User enters today's date as Date of Birth (newborn) |
| **Category** | ✅ Edge Case |
| **Preconditions** | Application is compiled and running |
| **Input** | *(today's date in DD/MM/YYYY format, e.g., `30/03/2026` if today is March 30, 2026)* |

**Expected Output:**

```
Your age is 0 years, 0 months, and 0 days.
```

**Pass Criteria:** `Period.between(today, today)` returns a `Period` with zero years, zero months, and zero days. The output shows all zeros. Critically, today's date is **NOT** rejected as a future date — the boundary between "valid" (today) and "future" (tomorrow) must be correct.

**Fail Criteria:** Application rejects today's date as a future date, crashes, or produces non-zero age values.

**Notes:** This verifies the boundary condition for the `isFutureDate()` check. The validation uses `date.isAfter(LocalDate.now())`, which returns `false` for today's date (today is not *after* today), so the date is correctly accepted.

---

### Test Case TC-018: Yesterday's Date (One Day Old)

| Field | Value |
|-------|-------|
| **Test ID** | TC-018 |
| **Scenario** | User enters yesterday's date |
| **Category** | ✅ Edge Case |
| **Preconditions** | Application is compiled and running |
| **Input** | *(yesterday's date in DD/MM/YYYY format, e.g., `29/03/2026` if today is March 30, 2026)* |

**Expected Output:**

```
Your age is 0 years, 0 months, and 1 days.
```

**Pass Criteria:** The age is exactly 0 years, 0 months, and 1 day. `Period.between(yesterday, today)` correctly produces this result.

**Fail Criteria:** Application produces incorrect values, rejects the date, or crashes.

---

### Test Case TC-019: Very Old Date

| Field | Value |
|-------|-------|
| **Test ID** | TC-019 |
| **Scenario** | User enters a very old date of birth |
| **Category** | ✅ Edge Case |
| **Preconditions** | Application is compiled and running |
| **Input** | `01/01/1900` |

**Expected Output:**

```
Your age is X years, Y months, and Z days.
```

*(For example, if the current date is March 30, 2026, the output would be approximately `Your age is 126 years, 2 months, and 29 days.`)*

**Pass Criteria:** Application handles very old dates without overflow or error. `Period.between()` computes correctly over large time spans (100+ years). The `java.time.LocalDate` class supports dates far in the past.

**Fail Criteria:** Application crashes with an overflow error, rejects the date, or produces incorrect values.

---

### Test Case TC-020: Same-Day Birthday (Birthday Today)

| Field | Value |
|-------|-------|
| **Test ID** | TC-020 |
| **Scenario** | User enters a date of birth that falls on today's month and day but in a past year (it is their birthday today) |
| **Category** | ✅ Edge Case |
| **Preconditions** | Application is compiled and running |
| **Input** | *(today's month and day with a past year, e.g., `30/03/1990` if today is March 30, 2026)* |

**Expected Output:**

```
Your age is X years, 0 months, and 0 days.
```

*(For example, `Your age is 36 years, 0 months, and 0 days.` if born on March 30, 1990 and today is March 30, 2026.)*

**Pass Criteria:** Months and days are both `0` on the exact anniversary of the birth date. Only the years component is non-zero. `Period.between()` correctly identifies the anniversary boundary.

**Fail Criteria:** Months or days are non-zero on the exact birthday, or the application produces incorrect year values.

---

### Test Case TC-021: End of Month Boundary

| Field | Value |
|-------|-------|
| **Test ID** | TC-021 |
| **Scenario** | User enters the last day of a month (January 31) |
| **Category** | ✅ Edge Case |
| **Preconditions** | Application is compiled and running |
| **Input** | `31/01/1998` |

**Expected Output:**

```
Your age is X years, Y months, and Z days.
```

**Pass Criteria:** Application correctly handles month-end dates. `Period.between()` accurately accounts for varying month lengths (28, 29, 30, or 31 days) when computing the age from a month-end birth date.

**Fail Criteria:** Application crashes, produces incorrect values due to month-length differences, or rejects a valid date.

**Notes:** Month-end boundaries can produce surprising results due to varying month lengths. For example, the period from January 31 to March 1 may differ depending on whether February has 28 or 29 days in the intervening year. The `java.time.Period` class handles these edge cases correctly.

---

## How to Execute Tests

Follow these steps to run each test case manually:

### Step 1: Compile the Project

From the **project root directory**, compile all Java source files:

```bash
javac src/*.java
```

This compiles `AgeCalculator.java`, `DateValidator.java`, and any other source files in the `src/` directory. JDK 8 or higher is required.

### Step 2: Run the Application

Launch the application from the project root directory:

```bash
java -cp src AgeCalculator
```

The application displays the prompt:

```
Enter your Date of Birth (DD/MM/YYYY): 
```

### Step 3: Enter the Test Input

Type the input specified for the test case (e.g., `15/08/1998` for TC-001) and press **Enter**.

### Step 4: Compare the Output

Compare the application's output with the **Expected Output** listed for the test case in this document.

- For **passing test cases** (✅), the output should match the format `Your age is X years, Y months, and Z days.` with mathematically correct values.
- For **failing test cases** (❌), the output should match the documented error message exactly.

### Step 5: Record the Result

Record the test result as **Pass** or **Fail** based on the comparison.

### Step 6: Repeat

The application processes **one Date of Birth per execution**. Re-run the application (Step 2) for each test case.

### Important Notes for Testers

- **Date-dependent tests:** For TC-011, TC-012, TC-017, TC-018, and TC-020, adjust the input dates based on the **current date** at test execution time. For example, TC-017 requires entering today's date, and TC-011 requires entering a date that is in the future relative to today.
- **Calculating expected values:** For passing test cases where the expected output includes `X years, Y months, and Z days`, compute the expected values using `Period.between(birthDate, LocalDate.now())` where `LocalDate.now()` is the current date when you run the test.
- **No automated framework:** This project does not use JUnit or any other automated test framework. All verification is performed by visual comparison of console output.

---

## Related Documentation

- [Usage Guide](../getting-started/usage.md) — Input format, output format, and error message details from the user's perspective
- [AgeCalculator API Reference](../api-reference/age-calculator.md) — Detailed documentation of the `calculateAge()` and `formatAge()` methods
- [DateValidator API Reference](../api-reference/date-validator.md) — Detailed documentation of validation methods (`parseDate()`, `isValidDate()`, `isFutureDate()`) and the error message catalog
- [DateUtils API Reference](../api-reference/date-utils.md) — Optional utility class for extended age calculations
- [Architecture Overview](../architecture/overview.md) — Validation flowchart, class diagram, and error handling architecture
- [Installation Guide](../getting-started/installation.md) — JDK setup and compilation instructions
- [README](../../README.md) — Project overview and quick start
