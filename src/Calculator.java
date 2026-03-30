import java.util.Scanner;

/**
 * A standard arithmetic calculator console application.
 *
 * <p>{@code Calculator} provides an interactive command-line interface for performing
 * basic arithmetic operations: addition, subtraction, multiplication, and division.
 * The user enters two numbers and selects an operation, and the calculator displays
 * the result.</p>
 *
 * <p>The calculator features:</p>
 * <ul>
 *   <li><strong>Four Operations</strong> — Addition (+), Subtraction (-),
 *       Multiplication (*), and Division (/)</li>
 *   <li><strong>Input Validation</strong> — Rejects non-numeric input and invalid
 *       operator selections with meaningful error messages</li>
 *   <li><strong>Division by Zero Protection</strong> — Detects and rejects division
 *       by zero with a clear error message</li>
 *   <li><strong>Decimal Support</strong> — Handles both integer and floating-point
 *       numbers using {@code double} precision</li>
 *   <li><strong>Continuous Mode</strong> — Allows the user to perform multiple
 *       calculations in a single session</li>
 *   <li><strong>Exception Handling</strong> — Proper {@code try-catch} blocks with
 *       meaningful error messages for all failure scenarios</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * javac src/Calculator.java
 * java -cp src Calculator
 * }</pre>
 *
 * <h2>Sample Interaction</h2>
 * <pre>
 * === Normal Calculator ===
 * Enter first number: 10
 * Enter second number: 5
 * Select operation (+, -, *, /): +
 * Result: 10.0 + 5.0 = 15.0
 *
 * Do you want to perform another calculation? (yes/no): no
 * Thank you for using the Calculator. Goodbye!
 * </pre>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @since JDK 8
 * @see AgeCalculator
 */
public class Calculator {

    /**
     * Application entry point. Presents an interactive calculator menu that
     * reads two numbers and an operator from the user, performs the calculation,
     * and displays the result. The user can perform multiple calculations in
     * a single session by answering "yes" when prompted to continue.
     *
     * <p>All exceptions are caught internally with meaningful error messages
     * displayed to the user. No exceptions propagate to the JVM.</p>
     *
     * @param args command-line arguments (unused by this application)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculating = true;

        System.out.println("=== Normal Calculator ===");

        while (continueCalculating) {
            try {
                // Read the first number from user input
                System.out.print("Enter first number: ");
                String firstInput = scanner.nextLine().trim();
                double num1 = parseNumber(firstInput);

                // Read the second number from user input
                System.out.print("Enter second number: ");
                String secondInput = scanner.nextLine().trim();
                double num2 = parseNumber(secondInput);

                // Read the operation from user input
                System.out.print("Select operation (+, -, *, /): ");
                String operator = scanner.nextLine().trim();
                validateOperator(operator);

                // Perform the calculation and display the result
                double result = calculate(num1, num2, operator);
                System.out.println("Result: " + formatExpression(num1, num2, operator, result));

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number input. Please enter a valid numeric value.");
            } catch (ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
            }

            // Ask the user if they want to continue
            System.out.print("\nDo you want to perform another calculation? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            continueCalculating = response.equals("yes") || response.equals("y");
        }

        System.out.println("Thank you for using the Calculator. Goodbye!");
        scanner.close();
    }

    /**
     * Performs the specified arithmetic operation on two numbers.
     *
     * <p>Supports four operations: addition ({@code +}), subtraction ({@code -}),
     * multiplication ({@code *}), and division ({@code /}). Division by zero is
     * detected and throws an {@link ArithmeticException} with a descriptive
     * message.</p>
     *
     * <p><strong>Example — Addition:</strong></p>
     * <pre>{@code
     * double result = Calculator.calculate(10.0, 5.0, "+");
     * // result: 15.0
     * }</pre>
     *
     * <p><strong>Example — Division by zero:</strong></p>
     * <pre>{@code
     * double result = Calculator.calculate(10.0, 0.0, "/");
     * // Throws ArithmeticException: Division by zero is not allowed.
     * }</pre>
     *
     * @param num1     the first operand
     * @param num2     the second operand
     * @param operator the arithmetic operator: {@code "+"}, {@code "-"},
     *                 {@code "*"}, or {@code "/"}
     * @return the result of the arithmetic operation as a {@code double}
     * @throws ArithmeticException      if the operator is {@code "/"} and
     *                                  {@code num2} is zero
     * @throws IllegalArgumentException if the operator is not one of
     *                                  {@code +}, {@code -}, {@code *}, {@code /}
     */
    public static double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return add(num1, num2);
            case "-":
                return subtract(num1, num2);
            case "*":
                return multiply(num1, num2);
            case "/":
                return divide(num1, num2);
            default:
                throw new IllegalArgumentException(
                    "Invalid operator: '" + operator + "'. Please use +, -, *, or /.");
        }
    }

    /**
     * Adds two numbers.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * double result = Calculator.add(10.0, 5.0); // 15.0
     * }</pre>
     *
     * @param a the first addend
     * @param b the second addend
     * @return the sum of {@code a} and {@code b}
     */
    public static double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts the second number from the first.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * double result = Calculator.subtract(10.0, 5.0); // 5.0
     * }</pre>
     *
     * @param a the minuend (number being subtracted from)
     * @param b the subtrahend (number being subtracted)
     * @return the difference {@code a - b}
     */
    public static double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two numbers.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * double result = Calculator.multiply(10.0, 5.0); // 50.0
     * }</pre>
     *
     * @param a the first factor
     * @param b the second factor
     * @return the product of {@code a} and {@code b}
     */
    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides the first number by the second.
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * double result = Calculator.divide(10.0, 5.0); // 2.0
     * }</pre>
     *
     * @param a the dividend (number being divided)
     * @param b the divisor (number to divide by)
     * @return the quotient {@code a / b}
     * @throws ArithmeticException if {@code b} is zero
     */
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }

    /**
     * Parses a string input into a {@code double} value.
     *
     * <p>Used internally by the {@code main} method to convert user-supplied
     * strings to numeric values. Throws {@link NumberFormatException} if the
     * input is not a valid number.</p>
     *
     * @param input the string to parse as a number
     * @return the parsed {@code double} value
     * @throws NumberFormatException    if the input is not a valid number
     * @throws IllegalArgumentException if the input is {@code null} or empty
     */
    public static double parseNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
        return Double.parseDouble(input.trim());
    }

    /**
     * Validates that the given operator is one of the supported arithmetic operators.
     *
     * <p>Supported operators: {@code +}, {@code -}, {@code *}, {@code /}.</p>
     *
     * @param operator the operator string to validate
     * @throws IllegalArgumentException if the operator is not supported or is
     *                                  {@code null}/empty
     */
    public static void validateOperator(String operator) {
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be null or empty.");
        }
        String trimmed = operator.trim();
        if (!trimmed.equals("+") && !trimmed.equals("-")
                && !trimmed.equals("*") && !trimmed.equals("/")) {
            throw new IllegalArgumentException(
                "Invalid operator: '" + trimmed + "'. Please use +, -, *, or /.");
        }
    }

    /**
     * Formats the calculation expression and result into a human-readable string.
     *
     * <p>The output follows the format: {@code num1 operator num2 = result}.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * String expr = Calculator.formatExpression(10.0, 5.0, "+", 15.0);
     * // expr: "10.0 + 5.0 = 15.0"
     * }</pre>
     *
     * @param num1     the first operand
     * @param num2     the second operand
     * @param operator the arithmetic operator used
     * @param result   the computed result
     * @return a formatted string representing the complete expression and its result
     */
    public static String formatExpression(double num1, double num2, String operator, double result) {
        return String.format("%s %s %s = %s",
            formatNumber(num1), operator, formatNumber(num2), formatNumber(result));
    }

    /**
     * Formats a {@code double} value for display, showing integers without
     * decimal places and non-integers with appropriate precision.
     *
     * <p>If the value is a whole number (e.g., {@code 15.0}), it is displayed
     * as {@code "15"}. Otherwise, it is displayed with its natural decimal
     * precision.</p>
     *
     * @param value the number to format
     * @return the formatted string representation
     */
    private static String formatNumber(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
