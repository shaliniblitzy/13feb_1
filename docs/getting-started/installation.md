# Installation Guide

This guide walks you through everything you need to set up and run the **Age Calculator** Java console application on your machine. It covers prerequisites, JDK installation, environment configuration, project compilation, and running the application for the first time.

> **Navigation:** [← Back to README](../../README.md) | [Usage Guide →](usage.md)

---

## Prerequisites

The Age Calculator requires **JDK 8 or higher** to compile and run. JDK 8 is the minimum supported version because the project relies on the `java.time` API, which was introduced in Java 8 as part of [JSR-310](https://jcp.org/en/jsr/detail?id=310).

### Required

- **Java Development Kit (JDK) 8+** — Includes both the `java` runtime and the `javac` compiler

### Java Standard Library APIs Used

The project uses the following APIs from the Java Standard Library — no external libraries are needed:

| API | Purpose |
|-----|---------|
| `java.time.LocalDate` | Core date representation for Date of Birth and the current date |
| `java.time.Period` | Age calculation in years, months, and days |
| `java.time.format.DateTimeFormatter` | Parsing user input in `DD/MM/YYYY` format |
| `java.util.Scanner` | Reading console input from the user |

### What You Do NOT Need

- **No build tools** — The project does not use Maven, Gradle, or Ant. Compilation is done directly with `javac`.
- **No external libraries or dependencies** — Every API used is part of the Java Standard Library.
- **No IDE required** — You can compile and run entirely from the command line. However, any Java IDE (IntelliJ IDEA, Eclipse, VS Code with Java extensions) will work if you prefer one.

---

## Installing JDK

You can install any JDK distribution that is version 8 or higher. The `java.time` API is available in all JDK versions from 8 onward, so JDK 11, JDK 17, and JDK 21 are all fully compatible.

### Recommended JDK Distributions

| Distribution | Download Link |
|-------------|---------------|
| Oracle JDK | [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/) |
| OpenJDK | [https://openjdk.org/](https://openjdk.org/) |
| Adoptium (Eclipse Temurin) | [https://adoptium.net/](https://adoptium.net/) |

Download the installer for your operating system, run it, and follow the on-screen instructions. Adoptium (Eclipse Temurin) is a popular open-source choice that provides easy-to-use installers for Windows, macOS, and Linux.

---

## Verifying Installation

After installing the JDK, open a terminal (or Command Prompt on Windows) and verify that both the runtime and the compiler are available.

### Verify the Java Runtime

```bash
java --version
```

> **Note:** On JDK 8, use `java -version` (single hyphen) instead of `java --version` (double hyphen).

You should see output similar to the following:

```
java version "17.0.x" 202x-xx-xx LTS
Java(TM) SE Runtime Environment (build 17.0.x+x-LTS-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+x-LTS-xxx, mixed mode, sharing)
```

### Verify the Java Compiler

```bash
javac --version
```

You should see output similar to:

```
javac 17.0.x
```

If either command prints `command not found` or `'java' is not recognized`, the JDK is not installed correctly or is not in your system `PATH`. See the [Environment Variables](#environment-variables) section below, or refer to [Troubleshooting](#troubleshooting) at the end of this guide.

---

## Environment Variables

Two environment variables ensure that the `java` and `javac` commands are available from any terminal session:

| Variable | Purpose |
|----------|---------|
| `JAVA_HOME` | Points to the root directory of your JDK installation. Some tools and IDEs rely on this variable to locate the JDK. |
| `PATH` | Must include the JDK `bin` directory so that `java`, `javac`, and `javadoc` commands are available globally. |

### Unix / macOS

Add the following lines to your shell profile file (e.g., `~/.bashrc`, `~/.zshrc`, or `~/.bash_profile`):

```bash
export JAVA_HOME=/path/to/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

Then reload your profile:

```bash
source ~/.bashrc
```

### Windows (Command Prompt)

```bash
set JAVA_HOME=C:\path\to\jdk
set PATH=%JAVA_HOME%\bin;%PATH%
```

To make these changes permanent on Windows, set them through **System Properties → Environment Variables** in the Control Panel.

---

## Compiling the Project

The Age Calculator uses direct `javac` compilation — no build tools are needed. Make sure you are in the **project root directory** before running any compilation commands.

### Compile Specific Files

```bash
# Compile the Age Calculator and its validator
javac src/AgeCalculator.java src/DateValidator.java

# Compile the Normal Calculator
javac src/Calculator.java
```

### Compile All Java Files at Once

```bash
javac src/*.java
```

Both commands produce `.class` bytecode files in the `src/` directory alongside the corresponding `.java` source files. These `.class` files are what the Java runtime executes.

### Common Compilation Errors

| Error Message | Cause | Solution |
|---------------|-------|----------|
| `javac: file not found: src/AgeCalculator.java` | You are not in the project root directory | Navigate to the project root with `cd` and try again |
| `error: release version X not supported` | The installed JDK version is too old | Install JDK 8 or higher |
| `error: cannot find symbol` | A required `.java` file was not included in the compilation | Use `javac src/*.java` to compile all files together |

---

## Running the Applications

After successful compilation, run either application with the following commands:

### Run the Age Calculator

```bash
java -cp src AgeCalculator
```

The `-cp src` flag sets the **classpath** to the `src/` directory, telling the Java runtime where to find the compiled `.class` files.

When the application starts, it prompts you to enter your Date of Birth:

```
Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
Your age is 27 years, 6 months, and 15 days.
```

### Run the Normal Calculator

```bash
java -cp src Calculator
```

When the calculator starts, it prompts you to enter two numbers and an operation:

```
=== Normal Calculator ===
Enter first number: 10
Enter second number: 5
Select operation (+, -, *, /): +
Result: 10 + 5 = 15
```

For detailed information about input format, output format, error messages, and advanced usage, see the [Usage Guide](usage.md).

---

## Generating Javadoc

The project includes Javadoc comments in its source files. You can generate browsable HTML API documentation with the following command:

```bash
javadoc -d docs/javadoc src/*.java
```

This command reads the Javadoc comments (`/** ... */`) from all `.java` files in `src/` and generates HTML documentation in the `docs/javadoc/` directory.

### Viewing the Generated Documentation

Open the following file in any web browser:

```
docs/javadoc/index.html
```

> **Note:** Generating Javadoc is optional — it is not required to compile or run the application. It is useful for developers who want to browse the API reference in a formatted, navigable web interface.

---

## Troubleshooting

### `java` is not recognized / command not found

**Cause:** The JDK is not installed, or the JDK `bin` directory is not in your system `PATH`.

**Solution:**
1. Verify the JDK is installed by checking if the `java` executable exists in your JDK installation directory (e.g., `/usr/lib/jvm/jdk-17/bin/java` or `C:\Program Files\Java\jdk-17\bin\java.exe`).
2. Add the JDK `bin` directory to your `PATH` environment variable. See [Environment Variables](#environment-variables) above.

### `javac` is not recognized / command not found

**Cause:** Only the JRE (Java Runtime Environment) is installed instead of the full JDK (Java Development Kit), or the JDK `bin` directory is not in your `PATH`.

**Solution:**
1. Ensure you have installed the **JDK** (which includes `javac`), not just the JRE (which only includes `java`).
2. Add the JDK `bin` directory to your `PATH` environment variable. See [Environment Variables](#environment-variables) above.

### Compilation Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `file not found` | Terminal is not in the project root directory | Run `cd` to navigate to the project root, then retry |
| `cannot find symbol` | Not all required `.java` files were compiled together | Use `javac src/*.java` to compile everything at once |
| `class is public, should be declared in a file named ...` | Class name does not match the filename | Ensure each public class name matches its `.java` filename exactly |

### Runtime Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `ClassNotFoundException` | The classpath does not include the directory containing `.class` files | Ensure you use `-cp src` when running: `java -cp src AgeCalculator` |
| `NoClassDefFoundError` | The class name does not match the filename (case-sensitive) | Verify that the class name `AgeCalculator` matches the filename `AgeCalculator.java` exactly, including capitalization |
| `Could not find or load main class` | The `.class` files were not generated, or the classpath is wrong | Recompile with `javac src/*.java` and run with `java -cp src AgeCalculator` |

---

## Project Structure

Below is the directory layout of the Age Calculator project, showing where source files, compiled files, and documentation reside:

```
age-calculator/
├── README.md
├── docs/
│   ├── getting-started/
│   │   ├── installation.md        ← You are here
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
    ├── DateUtils.java             (optional enhancement)
    └── Calculator.java
```

---

## Next Steps

Now that you have the project compiled and running, explore the rest of the documentation:

- **[Usage Guide](usage.md)** — How to use the applications, input format, error messages, and worked examples
- **[AgeCalculator API Reference](../api-reference/age-calculator.md)** — Detailed documentation of the `AgeCalculator` class and its methods
- **[Calculator API Reference](../api-reference/calculator.md)** — Detailed documentation of the `Calculator` class and its methods
- **[Architecture Overview](../architecture/overview.md)** — System design, class diagrams, and design decisions
- **[README](../../README.md)** — Project overview and quick start
