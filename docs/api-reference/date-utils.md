# DateUtils API Reference

> **Source:** `src/DateUtils.java`

`DateUtils` is an **optional reusable utility class** that provides a collection of `public static` methods for age-related date calculations beyond the core `AgeCalculator` functionality. While `AgeCalculator` focuses on console-based user interaction, `DateUtils` extracts and generalizes the calculation logic into standalone methods that can be imported and reused in other Java projects. All methods use the modern `java.time` API — specifically `java.time.LocalDate`, `java.time.Period`, and `java.time.temporal.ChronoUnit` — and are designed to be thread-safe, deterministic, and easy to test.

### Quick Navigation

- [AgeCalculator API](age-calculator.md) — Main application class with console I/O and age formatting
- [DateValidator API](date-validator.md) — Input validation methods for date parsing and checks
- [Architecture Overview](../architecture/overview.md) — Class diagrams, data flow, and design decisions
- [Optional Enhancements](../enhancements/optional-features.md) — Full enhancement guide including GUI and countdown features
- [README](../../README.md) — Project overview and quick start

---

## Class Overview

`DateUtils` encapsulates reusable date calculation logic as static utility methods. It is designed following the **utility class pattern**: all methods are `public static`, the constructor is `private` to prevent instantiation, and the class operates only on its method parameters and local variables — making it inherently thread-safe.

### Capabilities

- **Simplified age calculation** returning a formatted string (`X years, Y months, and Z days`) from a single `LocalDate` parameter, using `java.time.Period.between()` internally
- **Total age computation in months** using `java.time.temporal.ChronoUnit.MONTHS.between()` for scenarios that need a single numeric value
- **Total age computation in days** using `java.time.temporal.ChronoUnit.DAYS.between()` for precise day-level age tracking
- **Next birthday date calculation** with full leap year awareness — correctly handles February 29 birthdays in non-leap years
- **Countdown to next birthday** returning the number of days remaining until the next birthday, combining `nextBirthday()` with `ChronoUnit.DAYS.between()`

### Class Signature

```java
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    // Private constructor prevents instantiation
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class — cannot be instantiated.");
    }

    public static String calculateAge(LocalDate birthDate) { ... }
    public static long totalMonths(LocalDate birthDate) { ... }
    public static long totalDays(LocalDate birthDate) { ... }
    public static LocalDate nextBirthday(LocalDate birthDate) { ... }
    public static long daysUntilNextBirthday(LocalDate birthDate) { ... }
}
```

### Design Pattern

`DateUtils` follows the **utility class pattern** used throughout the Java Standard Library (for example, `java.util.Collections` and `java.util.Arrays`):

- All methods are `public static` — no instance of the class is needed
- A `private` constructor prevents accidental instantiation and subclassing
- Thread-safe by design: every method operates exclusively on its parameters and local variables, and all `java.time` objects (`LocalDate`, `Period`) are immutable

### Relationship with AgeCalculator

`DateUtils` extracts and generalizes the calculation logic from `AgeCalculator` for independent reuse:

- `AgeCalculator.calculateAge(LocalDate birthDate, LocalDate currentDate)` takes **two** `LocalDate` parameters for testability, returning a `Period` object
- `DateUtils.calculateAge(LocalDate birthDate)` takes **one** parameter and uses `LocalDate.now()` internally, returning a formatted `String` — optimized for convenience over testability
- The core application works fully without `DateUtils`; this class is an **optional enhancement** that adds utility methods for total months, total days, and birthday countdown functionality

> **Note:** This class is an optional enhancement. The core Age Calculator application functions completely without it. See [Optional Enhancements](../enhancements/optional-features.md) for guidance on integrating `DateUtils` into the project.

---

## Methods

### calculateAge

```java
public static String calculateAge(LocalDate birthDate)
```

**Description:**

Calculates the age from the given birth date to the current date and returns a formatted string. This is a simplified convenience wrapper around `java.time.Period.between(birthDate, LocalDate.now())`. The method internally obtains the current date via `LocalDate.now()`, computes the `Period`, and formats the result using the `Period` accessors `getYears()`, `getMonths()`, and `getDays()`.

**Javadoc Tags:**

- `@param birthDate` the date of birth as a `LocalDate` instance
- `@return` formatted age string in the pattern `X years, Y months, and Z days`
- `@throws IllegalArgumentException` if `birthDate` is `null` or in the future

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `birthDate` | `LocalDate` | The date of birth to calculate age from. Must not be `null` and must not be after the current date. |

**Returns:**

`String` — A formatted age string in the pattern `X years, Y months, and Z days` (for example, `27 years, 6 months, and 15 days`). The values are derived from `Period.getYears()`, `Period.getMonths()`, and `Period.getDays()`.

**Throws:**

| Exception | Condition |
|-----------|-----------|
| `IllegalArgumentException` | If `birthDate` is `null` |
| `IllegalArgumentException` | If `birthDate` is in the future (after `LocalDate.now()`) |

**Example 1 — Normal usage:**

```java
try {
    LocalDate dob = LocalDate.of(1998, 8, 15);
    String age = DateUtils.calculateAge(dob);
    System.out.println("Your age is " + age + ".");
    // Output: "Your age is 27 years, 6 months, and 15 days." (varies by current date)
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

> **Note:** The exact values in the output depend on the current system date when the method is called.

**Example 2 — Leap year edge case:**

```java
try {
    LocalDate dob = LocalDate.of(2000, 2, 29);
    String age = DateUtils.calculateAge(dob);
    System.out.println("Your age is " + age + ".");
    // Correctly handles leap year birth dates using Period.between()
    // The java.time API accounts for varying month lengths and leap years automatically
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

When the birth date is February 29 (a leap year date), `Period.between()` correctly handles the boundary in both leap and non-leap years. No special-case logic is required because the `java.time` API manages calendar complexities internally.

---

### totalMonths

```java
public static long totalMonths(LocalDate birthDate)
```

**Description:**

Calculates the total number of complete months from the birth date to the current date using `java.time.temporal.ChronoUnit.MONTHS.between(birthDate, LocalDate.now())`. Unlike `calculateAge()` which breaks the age into years, months, and days, this method returns a single `long` value representing the **total** elapsed months. For example, an age of 2 years and 3 months returns `27` (not `3`).

**Javadoc Tags:**

- `@param birthDate` the date of birth as a `LocalDate` instance
- `@return` total number of complete months since birth as a `long` value
- `@throws IllegalArgumentException` if `birthDate` is `null` or in the future

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `birthDate` | `LocalDate` | The date of birth. Must not be `null` and must not be after the current date. |

**Returns:**

`long` — The total number of complete months between the birth date and today. Partial months are not counted; only fully elapsed months contribute to the result.

**Throws:**

| Exception | Condition |
|-----------|-----------|
| `IllegalArgumentException` | If `birthDate` is `null` or in the future |

**Example 1 — Normal usage:**

```java
try {
    LocalDate dob = LocalDate.of(1998, 8, 15);
    long months = DateUtils.totalMonths(dob);
    System.out.println("Total months: " + months);
    // Output: "Total months: 318" (varies by current date)
    // 26 years × 12 months + 6 months = 318 total months (approximate)
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Example 2 — Edge case (born yesterday):**

```java
try {
    LocalDate dob = LocalDate.now().minusDays(1);
    long months = DateUtils.totalMonths(dob);
    System.out.println("Total months: " + months);
    // Output: "Total months: 0" (less than one complete month has elapsed)
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

When the birth date is less than one full month ago, `ChronoUnit.MONTHS.between()` returns `0` because only **complete** months are counted.

---

### totalDays

```java
public static long totalDays(LocalDate birthDate)
```

**Description:**

Calculates the total number of days from the birth date to the current date using `java.time.temporal.ChronoUnit.DAYS.between(birthDate, LocalDate.now())`. This method provides the most granular age measurement, counting every single day between the birth date and today.

**Javadoc Tags:**

- `@param birthDate` the date of birth as a `LocalDate` instance
- `@return` total number of days since birth as a `long` value
- `@throws IllegalArgumentException` if `birthDate` is `null` or in the future

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `birthDate` | `LocalDate` | The date of birth. Must not be `null` and must not be after the current date. |

**Returns:**

`long` — The total number of days between the birth date and today. This value accounts for leap years automatically — years with 366 days contribute one extra day to the total.

**Throws:**

| Exception | Condition |
|-----------|-----------|
| `IllegalArgumentException` | If `birthDate` is `null` or in the future |

**Example 1 — Normal usage:**

```java
try {
    LocalDate dob = LocalDate.of(1998, 8, 15);
    long days = DateUtils.totalDays(dob);
    System.out.println("Total days: " + days);
    // Output: "Total days: 9725" (varies by current date)
    // Includes leap year days automatically
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Example 2 — Edge case (same day birthday — born today):**

```java
try {
    LocalDate dob = LocalDate.now();
    long days = DateUtils.totalDays(dob);
    System.out.println("Total days: " + days);
    // Output: "Total days: 0" (born today, zero days have elapsed)
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

When the birth date is today, `ChronoUnit.DAYS.between()` returns `0`. This is a valid edge case representing a newborn or a same-day calculation check.

---

### nextBirthday

```java
public static LocalDate nextBirthday(LocalDate birthDate)
```

**Description:**

Calculates the date of the next upcoming birthday. If today is the person's birthday, this method returns today's date. The method determines the birthday in the current year; if that date has already passed, it rolls forward to the next year.

**Leap Year Handling:**

For persons born on February 29 (a leap year date), the behavior in non-leap years requires a design decision. There are two common approaches:

| Approach | Non-Leap Year Birthday | Rationale |
|----------|------------------------|-----------|
| **Approach A — March 1** | Returns March 1 of the current or next year | Treats the birthday as the day *after* February 28, which is the next valid calendar day. This is the simpler and more common approach. |
| **Approach B — Next Leap Year** | Returns February 29 of the next leap year | Waits for the actual calendar date to exist. This means the "next birthday" could be up to 4 years away. |

> **Design Decision:** The recommended implementation uses **Approach A** (March 1 in non-leap years). This provides a predictable annual birthday and avoids the awkward scenario where a person has no birthday for up to 3 years. The `java.time` API supports this via `birthDate.withYear(currentYear)`, which automatically adjusts February 29 to February 28 in non-leap years — the implementation can then add one day to arrive at March 1, or use `MonthDay` logic to decide the fallback.

**Javadoc Tags:**

- `@param birthDate` the date of birth as a `LocalDate` instance
- `@return` the date of the next birthday as a `LocalDate`
- `@throws IllegalArgumentException` if `birthDate` is `null` or in the future

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `birthDate` | `LocalDate` | The date of birth. Must not be `null` and must not be after the current date. |

**Returns:**

`LocalDate` — The date of the next upcoming birthday. If today is the birthday, returns today. For February 29 birthdays in non-leap years, returns March 1 of the current or next year (Approach A).

**Throws:**

| Exception | Condition |
|-----------|-----------|
| `IllegalArgumentException` | If `birthDate` is `null` or in the future |

**Example 1 — Normal usage:**

```java
try {
    LocalDate dob = LocalDate.of(1998, 8, 15);
    LocalDate next = DateUtils.nextBirthday(dob);
    System.out.println("Next birthday: " + next);
    // Output: "Next birthday: 2025-08-15" (varies by current date)
    // If today is before August 15, returns this year's date
    // If today is after August 15, returns next year's date
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Example 2 — Leap year edge case (February 29 birthday in a non-leap year):**

```java
try {
    LocalDate dob = LocalDate.of(2000, 2, 29);
    LocalDate next = DateUtils.nextBirthday(dob);
    System.out.println("Next birthday: " + next);
    // In a non-leap year: returns March 1 of the current or next year (Approach A)
    // In a leap year: returns February 29 of the current or next year
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

> **Leap Year Note:** When using Approach A, a person born on February 29, 2000 would have their "birthday" on March 1, 2025 (a non-leap year) and on February 29, 2028 (the next leap year). The implementation should document which approach it uses so callers can set correct expectations.

---

### daysUntilNextBirthday

```java
public static long daysUntilNextBirthday(LocalDate birthDate)
```

**Description:**

Calculates the number of days remaining until the next birthday by combining `nextBirthday(birthDate)` with `java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), nextBirthday(birthDate))`. Returns `0` if today is the person's birthday. The maximum return value is `366` for leap year birthdays when using Approach A (March 1 fallback in non-leap years), or up to approximately `1461` (4 years) when using Approach B (next leap year).

**Javadoc Tags:**

- `@param birthDate` the date of birth as a `LocalDate` instance
- `@return` the number of days until the next birthday as a `long` value (range: 0–366)
- `@throws IllegalArgumentException` if `birthDate` is `null` or in the future

**Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `birthDate` | `LocalDate` | The date of birth. Must not be `null` and must not be after the current date. |

**Returns:**

`long` — The number of days remaining until the next birthday. Returns `0` if today is the birthday. The typical range is 0–365 (or 0–366 in years that contain a leap day between now and the next birthday).

**Throws:**

| Exception | Condition |
|-----------|-----------|
| `IllegalArgumentException` | If `birthDate` is `null` or in the future |

**Example 1 — Normal usage:**

```java
try {
    LocalDate dob = LocalDate.of(1998, 8, 15);
    long days = DateUtils.daysUntilNextBirthday(dob);
    System.out.println("Days until next birthday: " + days);
    // Output varies by current date
    // If today is August 1: "Days until next birthday: 14"
    // If today is August 15: "Days until next birthday: 0" (birthday is today)
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

**Example 2 — Birthday is today:**

```java
try {
    LocalDate dob = LocalDate.now().minusYears(25);
    long days = DateUtils.daysUntilNextBirthday(dob);
    System.out.println("Days until next birthday: " + days);
    // Output: "Days until next birthday: 0"
    // When today is the birthday, the method returns 0
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

When today is the person's birthday, `nextBirthday()` returns today's date, and `ChronoUnit.DAYS.between(today, today)` returns `0`. This confirms the birthday countdown has reached zero.

---

## Method Summary

| Method | Return Type | Description |
|--------|-------------|-------------|
| `calculateAge(LocalDate)` | `String` | Returns a formatted age string in the pattern `X years, Y months, and Z days` |
| `totalMonths(LocalDate)` | `long` | Returns the total number of complete months since birth via `ChronoUnit.MONTHS` |
| `totalDays(LocalDate)` | `long` | Returns the total number of days since birth via `ChronoUnit.DAYS` |
| `nextBirthday(LocalDate)` | `LocalDate` | Returns the date of the next upcoming birthday with leap year awareness |
| `daysUntilNextBirthday(LocalDate)` | `long` | Returns the number of days remaining until the next birthday (0–366) |

All methods accept a single `LocalDate` parameter representing the date of birth, validate that it is not `null` and not in the future, and throw `IllegalArgumentException` on invalid input.

---

## Integration Examples

### Using DateUtils in Another Project

The following example demonstrates how to import and use `DateUtils` in a separate Java application. The example uses `DateTimeFormatter` to parse a Date of Birth string in `DD/MM/YYYY` format and then calls every `DateUtils` method:

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MyApp {
    public static void main(String[] args) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse("15/08/1998", formatter);

            System.out.println("Age: " + DateUtils.calculateAge(dob));
            System.out.println("Total months: " + DateUtils.totalMonths(dob));
            System.out.println("Total days: " + DateUtils.totalDays(dob));
            System.out.println("Next birthday: " + DateUtils.nextBirthday(dob));
            System.out.println("Days until birthday: " + DateUtils.daysUntilNextBirthday(dob));
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use DD/MM/YYYY.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
```

**Expected output (values vary by current date):**

```
Age: 27 years, 6 months, and 15 days
Total months: 318
Total days: 9725
Next birthday: 2025-08-15
Days until birthday: 138
```

### Combining with DateValidator

For robust input handling, combine `DateUtils` with `DateValidator` to validate user input before performing calculations. `DateValidator` handles format parsing and future date rejection, while `DateUtils` performs the calculations:

```java
try {
    String input = "15/08/1998";
    LocalDate dob = DateValidator.parseDate(input);

    if (DateValidator.isFutureDate(dob)) {
        System.out.println("Error: Date of Birth cannot be in the future.");
    } else {
        System.out.println("Your age is " + DateUtils.calculateAge(dob) + ".");
        System.out.println("Total months alive: " + DateUtils.totalMonths(dob));
        System.out.println("Total days alive: " + DateUtils.totalDays(dob));
        System.out.println("Next birthday: " + DateUtils.nextBirthday(dob));
        System.out.println("Days until next birthday: " + DateUtils.daysUntilNextBirthday(dob));
    }
} catch (DateTimeParseException e) {
    System.out.println("Error: Invalid date format. Please use DD/MM/YYYY.");
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
} catch (Exception e) {
    System.out.println("An unexpected error occurred: " + e.getMessage());
}
```

> **Tip:** Always validate input with `DateValidator` before passing it to `DateUtils`. While `DateUtils` methods perform their own null and future-date checks, using `DateValidator` first provides user-friendly error messages specific to input format issues.

---

## Error Handling

All `DateUtils` methods follow a consistent validation-first pattern. Before performing any calculation, each method validates its input and throws an `IllegalArgumentException` with a descriptive message if the input is invalid.

### Error Message Catalog

| Method | Error Condition | Error Message |
|--------|----------------|---------------|
| All methods | `birthDate` is `null` | `"Birth date must not be null."` |
| All methods | `birthDate` is after `LocalDate.now()` | `"Birth date cannot be in the future."` |

### Recommended Exception Handling Pattern

Every call to a `DateUtils` method should be wrapped in a `try-catch` block:

```java
try {
    String result = DateUtils.calculateAge(birthDate);
    System.out.println("Your age is " + result + ".");
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
} catch (Exception e) {
    System.out.println("An unexpected error occurred: " + e.getMessage());
}
```

> **Best Practice:** Catch `IllegalArgumentException` specifically to handle known validation errors, and use a general `Exception` catch-all for unexpected situations. This pattern ensures the user always receives a meaningful error message.

---

## Design Notes

### Utility Class Pattern

`DateUtils` follows the standard Java utility class pattern:

- **All methods are `public static`** — callers use `DateUtils.calculateAge(dob)` without creating an instance
- **Private constructor** prevents accidental instantiation: `new DateUtils()` throws `UnsupportedOperationException`
- **Thread-safe by design** — every method uses only local variables and `java.time` immutable classes (`LocalDate`, `Period`). No shared mutable state exists, so concurrent access from multiple threads is safe without synchronization
- **No external dependencies** — the class uses only the Java Standard Library (`java.time.LocalDate`, `java.time.Period`, `java.time.temporal.ChronoUnit`)

### Relationship to AgeCalculator

| Aspect | `AgeCalculator.calculateAge()` | `DateUtils.calculateAge()` |
|--------|-------------------------------|---------------------------|
| Parameters | Two: `LocalDate birthDate`, `LocalDate currentDate` | One: `LocalDate birthDate` |
| Current date | Passed as a parameter (deterministic, testable) | Obtained internally via `LocalDate.now()` (convenient) |
| Return type | `Period` (raw data) | `String` (formatted for display) |
| Purpose | Core calculation with maximum testability | Convenience wrapper for quick use |

`DateUtils` is designed for **convenience** — one parameter, one return value, formatted for immediate display. `AgeCalculator.calculateAge()` is designed for **testability** — accepting the current date as a parameter makes unit tests deterministic and reproducible.

### Optional Enhancement Status

This class is an **optional enhancement** to the core Age Calculator application. The core application (`AgeCalculator` + `DateValidator`) works fully without `DateUtils`. The utility class adds value in these scenarios:

- Projects that need to reuse age calculation logic without the console I/O layer
- Applications that require total age in months or days rather than the years/months/days breakdown
- Features that need birthday countdown functionality (e.g., birthday reminder applications)

For implementation guidance and additional optional features (GUI, extended calculations), see [Optional Enhancements](../enhancements/optional-features.md).

---

## See Also

- [AgeCalculator API Reference](age-calculator.md) — Main application class with `calculateAge(LocalDate, LocalDate)` and `formatAge(Period)` methods
- [DateValidator API Reference](date-validator.md) — Input validation class with `parseDate()`, `isValidDate()`, and `isFutureDate()` methods
- [Architecture Overview](../architecture/overview.md) — Class diagram showing the relationships between `AgeCalculator`, `DateValidator`, and `DateUtils`
- [Optional Enhancements](../enhancements/optional-features.md) — Full enhancement guide covering GUI, utility class integration, and birthday countdown
- [README](../../README.md) — Project overview and quick start guide
