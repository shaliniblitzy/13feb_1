# Age Calculator & Normal Calculator — Java Console Applications

A collection of Java console applications featuring an **Age Calculator** that computes a user's exact age in years, months, and days from their Date of Birth, and a **Normal Calculator** that performs basic arithmetic operations (addition, subtraction, multiplication, division). Built using the `java.time` API (Java 8+) with proper input validation, OOP principles, and comprehensive error handling.

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Age Calculator Usage](#age-calculator-usage)
- [Normal Calculator Usage](#normal-calculator-usage)
- [Test Cases](#test-cases)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Optional Enhancements](#optional-enhancements)
- [Documentation](#documentation)
- [License](#license)
- [Author](#author)

### External Documentation

- [Installation Guide](docs/getting-started/installation.md)
- [Usage Guide](docs/getting-started/usage.md)
- [AgeCalculator API](docs/api-reference/age-calculator.md)
- [DateValidator API](docs/api-reference/date-validator.md)
- [DateUtils API](docs/api-reference/date-utils.md)
- [Calculator API](docs/api-reference/calculator.md)
- [Architecture](docs/architecture/overview.md)
- [Test Cases](docs/testing/test-cases.md)
- [Optional Enhancements](docs/enhancements/optional-features.md)

---

## Features

### Age Calculator
- **Exact Age Calculation** — Calculates age in years, months, and days using `java.time.Period`
- **Standard Input Format** — Accepts Date of Birth in **DD/MM/YYYY** format
- **Input Validation** — Rejects invalid dates, future dates, and malformed input with clear error messages
- **Leap Year Handling** — Correctly processes leap year dates (e.g., 29/02/2000)
- **Clean Output** — Displays results in the format: `Your age is X years, Y months, and Z days.`

### Normal Calculator
- **Four Arithmetic Operations** — Addition (`+`), Subtraction (`-`), Multiplication (`*`), and Division (`/`)
- **Input Validation** — Rejects non-numeric input and invalid operator selections with meaningful error messages
- **Division by Zero Protection** — Detects and rejects division by zero with a clear error message
- **Decimal Support** — Handles both integer and floating-point numbers using `double` precision
- **Continuous Mode** — Perform multiple calculations in a single session
- **Clean Number Formatting** — Displays whole numbers without unnecessary decimal places

### Shared
- **Exception Handling** — Proper `try-catch` blocks with meaningful error messages for all failure scenarios
- **Object-Oriented Design** — Follows OOP principles with separation of concerns across dedicated classes

---

## Prerequisites

- **JDK 8 or higher** — Required for the `java.time` API (`LocalDate`, `Period`, `DateTimeFormatter`)

Verify your Java installation:

```bash
java --version
# or
java -version
```

> **Note:** The `java.time` package was introduced in Java 8 (JSR-310). Ensure your JDK version is 8 or above before compiling.

---

## Quick Start

### Compile

```bash
# Compile all Java source files at once
javac src/*.java
```

### Run the Age Calculator

```bash
java -cp src AgeCalculator
```

### Run the Normal Calculator

```bash
java -cp src Calculator
```

---

## Age Calculator Usage

When you run the Age Calculator, you will be prompted to enter your Date of Birth in **DD/MM/YYYY** format. The application calculates and displays your exact age.

### Sample Session

```
Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
Your age is 27 years, 6 months, and 15 days.
```

### Error Handling Examples

**Invalid date format:**

```
Enter your Date of Birth (DD/MM/YYYY): 2020-08-15
Error: Invalid date format. Please use DD/MM/YYYY.
```

**Invalid calendar date:**

```
Enter your Date of Birth (DD/MM/YYYY): 31/02/2020
Error: Invalid calendar date. The date does not exist on the calendar.
```

**Future date:**

```
Enter your Date of Birth (DD/MM/YYYY): 25/12/2030
Error: Date of Birth cannot be in the future.
```

For more detailed usage instructions and examples, see the [Usage Guide](docs/getting-started/usage.md).

---

## Normal Calculator Usage

When you run the Normal Calculator, you will be prompted to enter two numbers and select an arithmetic operation. The calculator displays the result and offers to perform another calculation.

### Sample Session

```
=== Normal Calculator ===
Enter first number: 10
Enter second number: 5
Select operation (+, -, *, /): +
Result: 10 + 5 = 15

Do you want to perform another calculation? (yes/no): no
Thank you for using the Calculator. Goodbye!
```

### Error Handling Examples

**Division by zero:**

```
=== Normal Calculator ===
Enter first number: 10
Enter second number: 0
Select operation (+, -, *, /): /
Error: Division by zero is not allowed.
```

**Invalid number input:**

```
=== Normal Calculator ===
Enter first number: abc
Error: Invalid number input. Please enter a valid numeric value.
```

**Invalid operator:**

```
=== Normal Calculator ===
Enter first number: 10
Enter second number: 5
Select operation (+, -, *, /): %
Error: Invalid operator: '%'. Please use +, -, *, or /.
```

For detailed API documentation, see the [Calculator API Reference](docs/api-reference/calculator.md).

---

## Test Cases

### Age Calculator Test Cases

| # | Scenario | Input | Expected Result |
|---|----------|-------|-----------------|
| 1 | ✅ Normal DOB | `15/08/1998` | Displays age in years, months, and days |
| 2 | ✅ Leap year DOB | `29/02/2000` | Correctly handles leap year birth date |
| 3 | ❌ Invalid date | `31/02/2020` | Error: Invalid calendar date. The date does not exist on the calendar. |
| 4 | ❌ Future date | *(any future date)* | Error: Date of Birth cannot be in the future. |
| 5 | ❌ Wrong format | `abc/xyz` | Error: Invalid date format. Please use DD/MM/YYYY. |

### Normal Calculator Test Cases

| # | Scenario | Input | Expected Result |
|---|----------|-------|-----------------|
| 1 | ✅ Addition | `10`, `5`, `+` | `10 + 5 = 15` |
| 2 | ✅ Subtraction | `20`, `4`, `-` | `20 - 4 = 16` |
| 3 | ✅ Multiplication | `6`, `7`, `*` | `6 * 7 = 42` |
| 4 | ✅ Division | `15`, `4`, `/` | `15 / 4 = 3.75` |
| 5 | ❌ Division by zero | `10`, `0`, `/` | Error: Division by zero is not allowed. |
| 6 | ❌ Invalid number | `abc`, `5`, `+` | Error: Invalid number input. |
| 7 | ❌ Invalid operator | `10`, `5`, `%` | Error: Invalid operator. |

For detailed test scenarios, expected outputs, and edge cases, see [Test Cases](docs/testing/test-cases.md).

---

## Project Structure

```
age-calculator/
├── README.md
├── docs/
│   ├── getting-started/
│   │   ├── installation.md
│   │   └── usage.md
│   ├── api-reference/
│   │   ├── age-calculator.md
│   │   ├── date-validator.md
│   │   ├── date-utils.md
│   │   └── calculator.md
│   ├── architecture/
│   │   └── overview.md
│   ├── testing/
│   │   └── test-cases.md
│   └── enhancements/
│       └── optional-features.md
└── src/
    ├── AgeCalculator.java
    ├── DateValidator.java
    ├── DateUtils.java
    └── Calculator.java
```

| Directory / File | Description |
|------------------|-------------|
| `README.md` | Project overview and quick-start guide (this file) |
| `docs/getting-started/` | Installation and usage guides |
| `docs/api-reference/` | API reference for all public classes |
| `docs/architecture/` | Architecture overview with class diagrams |
| `docs/testing/` | Test case matrix and scenario documentation |
| `docs/enhancements/` | Optional feature enhancement guide |
| `src/AgeCalculator.java` | Main age calculator class — entry point, age calculation, and output formatting |
| `src/DateValidator.java` | Input validation — date parsing, format checking, and future date detection |
| `src/DateUtils.java` | Reusable utility class for age operations (optional enhancement) |
| `src/Calculator.java` | Normal arithmetic calculator — addition, subtraction, multiplication, and division |

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | Java 8+ |
| **Core APIs** | `java.time.LocalDate`, `java.time.Period`, `java.time.format.DateTimeFormatter` |
| **Input** | `java.util.Scanner` for console input |
| **Build** | Direct `javac` compilation (no Maven/Gradle required) |
| **Documentation** | Markdown with Mermaid diagrams, Javadoc for inline API reference |

---

## Optional Enhancements

The following enhancements extend the core Age Calculator with additional functionality:

- **Total Age in Months and Days** — Display the total age expressed entirely in months or entirely in days using `java.time.temporal.ChronoUnit`
- **Countdown to Next Birthday** — Calculate the next birthday date and the number of days remaining until it arrives
- **Java Swing / JavaFX GUI** — Replace the console interface with a graphical form featuring an input field, a calculate button, and a result display label
- **Reusable `DateUtils` Utility Class** — Extract age calculation logic into a standalone utility class that can be imported and reused in other Java projects

See [Optional Enhancements](docs/enhancements/optional-features.md) for detailed implementation guidance, code examples, and architectural considerations for each feature.

---

## Documentation

Comprehensive documentation is available in the `docs/` directory:

| Document | Path | Description |
|----------|------|-------------|
| Installation Guide | [docs/getting-started/installation.md](docs/getting-started/installation.md) | JDK setup and project compilation |
| Usage Guide | [docs/getting-started/usage.md](docs/getting-started/usage.md) | Input format, running the apps, and examples |
| AgeCalculator API | [docs/api-reference/age-calculator.md](docs/api-reference/age-calculator.md) | Age calculator class API reference |
| DateValidator API | [docs/api-reference/date-validator.md](docs/api-reference/date-validator.md) | Validation class API reference |
| DateUtils API | [docs/api-reference/date-utils.md](docs/api-reference/date-utils.md) | Utility class API reference |
| Calculator API | [docs/api-reference/calculator.md](docs/api-reference/calculator.md) | Normal calculator class API reference |
| Architecture Overview | [docs/architecture/overview.md](docs/architecture/overview.md) | Class diagrams and design decisions |
| Test Cases | [docs/testing/test-cases.md](docs/testing/test-cases.md) | Test matrix and scenarios |
| Optional Features | [docs/enhancements/optional-features.md](docs/enhancements/optional-features.md) | Enhancement guide |

---

## License

This project is provided for educational and portfolio purposes. You are free to use, modify, and distribute this code for learning, interview preparation, and personal projects.

---

## Author

Age Calculator & Normal Calculator — Java mini-projects for demonstrating date handling, arithmetic operations, input validation, and Object-Oriented Programming principles using the `java.time` API.
