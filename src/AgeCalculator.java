import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main application class for the Java Age Calculator console application.
 *
 * <p>{@code AgeCalculator} serves as the entry point that reads a Date of Birth
 * from the user via standard input, validates the input by delegating to
 * {@link DateValidator}, calculates the exact age in years, months, and days
 * using {@link java.time.Period#between(LocalDate, LocalDate)}, and outputs
 * the result in a formatted string.</p>
 *
 * <p>All operations use the modern {@code java.time} API introduced in Java 8
 * (JSR-310). The legacy {@code java.util.Date} and {@code java.util.Calendar}
 * are never used.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * javac src/AgeCalculator.java src/DateValidator.java
 * java -cp src AgeCalculator
 * }</pre>
 *
 * <h2>Sample Interaction</h2>
 * <pre>
 * Enter your Date of Birth (DD/MM/YYYY): 15/08/1998
 * Your age is 27 years, 6 months, and 15 days.
 * </pre>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @since JDK 8
 * @see DateValidator
 * @see DateUtils
 */
public class AgeCalculator {

    /**
     * Application entry point. Creates a {@link Scanner} for console input,
     * prompts the user for their Date of Birth in DD/MM/YYYY format, validates
     * the input using {@link DateValidator}, calculates the age using
     * {@link #calculateAge(LocalDate, LocalDate)}, formats the result using
     * {@link #formatAge(Period)}, and displays the output.
     *
     * <p>All exceptions are caught internally with meaningful error messages
     * displayed to the user. No exceptions propagate to the JVM.</p>
     *
     * <p><strong>Application Flow:</strong></p>
     * <ol>
     *   <li>Prompt: Display {@code Enter your Date of Birth (DD/MM/YYYY): }</li>
     *   <li>Read input via {@link Scanner#nextLine()}</li>
     *   <li>Pre-validate format: {@link DateValidator#isValidDate(String)}</li>
     *   <li>Parse with strict validation: {@link DateValidator#parseDate(String)}</li>
     *   <li>Check for future date: {@link DateValidator#isFutureDate(LocalDate)}</li>
     *   <li>Calculate age: {@link #calculateAge(LocalDate, LocalDate)}</li>
     *   <li>Format output: {@link #formatAge(Period)}</li>
     *   <li>Display result to console</li>
     * </ol>
     *
     * @param args command-line arguments (unused by this application)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your Date of Birth (DD/MM/YYYY): ");
        String input = scanner.nextLine();

        try {
            // Step 1: Pre-validate format (basic DD/MM/YYYY pattern check)
            if (!DateValidator.isValidDate(input)) {
                System.out.println("Error: Invalid date format. Please use DD/MM/YYYY.");
                return;
            }
            // Step 2: Parse with strict resolver (catches invalid calendar dates like Feb 31)
            LocalDate birthDate = DateValidator.parseDate(input);
            // Step 3: Check for future dates (boolean check, no exception thrown)
            if (DateValidator.isFutureDate(birthDate)) {
                System.out.println("Error: Date of Birth cannot be in the future.");
                return;
            }

            Period age = calculateAge(birthDate, LocalDate.now());
            System.out.println(formatAge(age));
        } catch (DateTimeParseException e) {
            // If isValidDate passed but parseDate threw, the date has a valid format
            // but does not exist on the calendar (e.g., 31/02/2020).
            System.out.println("Error: Invalid calendar date. The date does not exist on the calendar.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            // Catch-all: uses a generic message to avoid exposing raw exception details.
            System.out.println("An unexpected error occurred. Please try again.");
        } finally {
            scanner.close();
        }
    }

    /**
     * Calculates the exact age as a {@link java.time.Period} object representing the
     * chronological difference in years, months, and days between the birth date and
     * the specified current date.
     *
     * <p>Internally, this method calls {@link Period#between(LocalDate, LocalDate)}
     * from the {@code java.time} API. The resulting {@code Period} can be queried
     * with {@link Period#getYears()}, {@link Period#getMonths()}, and
     * {@link Period#getDays()} to extract individual components.</p>
     *
     * <p><strong>Example — Normal usage:</strong></p>
     * <pre>{@code
     * LocalDate birthDate = LocalDate.of(1998, 8, 15);
     * LocalDate currentDate = LocalDate.now();
     * Period age = AgeCalculator.calculateAge(birthDate, currentDate);
     * System.out.println("Years: " + age.getYears());
     * }</pre>
     *
     * <p><strong>Example — Leap year edge case:</strong></p>
     * <pre>{@code
     * LocalDate birthDate = LocalDate.of(2000, 2, 29); // Leap year birthday
     * LocalDate currentDate = LocalDate.of(2025, 2, 28);
     * Period age = AgeCalculator.calculateAge(birthDate, currentDate);
     * // Period.between() handles leap year boundaries correctly
     * }</pre>
     *
     * @param birthDate   the date of birth as a {@link LocalDate}
     * @param currentDate the reference date for age calculation (usually today
     *                    via {@link LocalDate#now()})
     * @return a {@link Period} object representing the age in years, months, and days
     * @throws IllegalArgumentException if either parameter is {@code null} or if
     *         {@code birthDate} is after {@code currentDate}
     */
    public static Period calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate == null || currentDate == null) {
            throw new IllegalArgumentException("Birth date and current date must not be null.");
        }
        if (birthDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Birth date cannot be after the current date.");
        }
        return Period.between(birthDate, currentDate);
    }

    /**
     * Formats a {@link java.time.Period} object into a human-readable age string.
     *
     * <p>The output follows the <strong>exact format</strong> specified for this
     * application:</p>
     * <pre>Your age is X years, Y months, and Z days.</pre>
     *
     * <p>This method uses {@link String#format(String, Object...)} with the
     * {@code Period} accessors {@link Period#getYears()}, {@link Period#getMonths()},
     * and {@link Period#getDays()} to construct the result.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * Period age = Period.of(27, 6, 15);
     * String result = AgeCalculator.formatAge(age);
     * // result: "Your age is 27 years, 6 months, and 15 days."
     * }</pre>
     *
     * @param age the {@link Period} object representing the calculated age
     * @return a formatted string in the pattern
     *         {@code Your age is X years, Y months, and Z days.}
     * @throws IllegalArgumentException if {@code age} is {@code null}
     */
    public static String formatAge(Period age) {
        if (age == null) {
            throw new IllegalArgumentException("Age period must not be null.");
        }
        return String.format("Your age is %d years, %d months, and %d days.",
            age.getYears(), age.getMonths(), age.getDays());
    }
}
