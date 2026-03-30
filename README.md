# Age Calculator — Java Console Application

A Java console application that calculates a user's exact age in years, months, and days from their Date of Birth (DOB). Built using the `java.time` API (Java 8+) with proper input validation and OOP principles.

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Usage](#usage)
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
- [API Reference](docs/api-reference/age-calculator.md)
- [Architecture](docs/architecture/overview.md)
- [Test Cases](docs/testing/test-cases.md)
- [Optional Enhancements](docs/enhancements/optional-features.md)

---

## Features

- **Exact Age Calculation** — Calculates age in years, months, and days using `java.time.Period`
- **Standard Input Format** — Accepts Date of Birth in **DD/MM/YYYY** format
- **Input Validation** — Rejects invalid dates, future dates, and malformed input with clear error messages
- **Leap Year Handling** — Correctly processes leap year dates (e.g., 29/02/2000)
- **Clean Output** — Displays results in the format: `Your age is X years, Y months, and Z days.`
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
# Compile the main application and validator
javac src/AgeCalculator.java src/DateValidator.java

# Or compile all Java source files at once
javac src/*.java
```

### Run

```bash
java -cp src AgeCalculator
```

---

## Usage

When you run the application, you will be prompted to enter your Date of Birth in **DD/MM/YYYY** format. The application calculates and displays your exact age.

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
Error: Invalid calendar date.
```

**Future date:**

```
Enter your Date of Birth (DD/MM/YYYY): 25/12/2030
Error: Date of Birth cannot be in the future.
```

For more detailed usage instructions and examples, see the [Usage Guide](docs/getting-started/usage.md).

---

## Test Cases

The following test scenarios verify the correctness of the Age Calculator:

| # | Scenario | Input | Expected Result |
|---|----------|-------|-----------------|
| 1 | ✅ Normal DOB | `15/08/1998` | Displays age in years, months, and days |
| 2 | ✅ Leap year DOB | `29/02/2000` | Correctly handles leap year birth date |
| 3 | ❌ Invalid date | `31/02/2020` | Error: Invalid calendar date |
| 4 | ❌ Future date | *(any future date)* | Error: Date cannot be in the future |
| 5 | ❌ Wrong format | `abc/xyz` | Error: Invalid date format |

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
│   │   └── date-utils.md
│   ├── architecture/
│   │   └── overview.md
│   ├── testing/
│   │   └── test-cases.md
│   └── enhancements/
│       └── optional-features.md
└── src/
    ├── AgeCalculator.java
    ├── DateValidator.java
    └── DateUtils.java
```

| Directory / File | Description |
|------------------|-------------|
| `README.md` | Project overview and quick-start guide (this file) |
| `docs/getting-started/` | Installation and usage guides |
| `docs/api-reference/` | API reference for all public classes |
| `docs/architecture/` | Architecture overview with class diagrams |
| `docs/testing/` | Test case matrix and scenario documentation |
| `docs/enhancements/` | Optional feature enhancement guide |
| `src/AgeCalculator.java` | Main application class — entry point, age calculation, and output formatting |
| `src/DateValidator.java` | Input validation — date parsing, format checking, and future date detection |
| `src/DateUtils.java` | Reusable utility class for age operations (optional enhancement) |

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
| Usage Guide | [docs/getting-started/usage.md](docs/getting-started/usage.md) | Input format, running the app, and examples |
| AgeCalculator API | [docs/api-reference/age-calculator.md](docs/api-reference/age-calculator.md) | Main class API reference |
| DateValidator API | [docs/api-reference/date-validator.md](docs/api-reference/date-validator.md) | Validation class API reference |
| DateUtils API | [docs/api-reference/date-utils.md](docs/api-reference/date-utils.md) | Utility class API reference |
| Architecture Overview | [docs/architecture/overview.md](docs/architecture/overview.md) | Class diagrams and design decisions |
| Test Cases | [docs/testing/test-cases.md](docs/testing/test-cases.md) | Test matrix and scenarios |
| Optional Features | [docs/enhancements/optional-features.md](docs/enhancements/optional-features.md) | Enhancement guide |

---

## License

This project is provided for educational and portfolio purposes. You are free to use, modify, and distribute this code for learning, interview preparation, and personal projects.

---

## Author

Age Calculator — A Java mini-project for demonstrating date handling, input validation, and Object-Oriented Programming principles using the `java.time` API.
