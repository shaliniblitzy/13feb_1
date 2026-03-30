import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Input validation component for the Java Age Calculator application.
 *
 * <p>{@code DateValidator} handles all input validation, ensuring that only valid,
 * parseable, past-or-present dates are accepted before age calculation proceeds.
 * Every user-supplied Date of Birth string passes through {@code DateValidator}
 * before reaching the age calculation logic in {@link AgeCalculator}.</p>
 *
 * <p>The class fulfils three core responsibilities:</p>
 * <ol>
 *   <li><strong>Parsing user input</strong> from a {@code DD/MM/YYYY} format string
 *       into a {@link java.time.LocalDate} object</li>
 *   <li><strong>Validating calendar correctness</strong> — rejecting dates that do not
 *       exist on the calendar (e.g., {@code 31/02/2020})</li>
 *   <li><strong>Rejecting future dates</strong> — Date of Birth cannot be after today's date</li>
 * </ol>
 *
 * <p><strong>Important — {@code uuuu} vs {@code yyyy} with {@code ResolverStyle.STRICT}:</strong>
 * The pattern uses {@code uuuu} (proleptic year) instead of {@code yyyy} (year-of-era)
 * because {@code ResolverStyle.STRICT} requires an era designator field ({@code G}) when
 * using {@code yyyy}. The proleptic year field {@code uuuu} works correctly with
 * {@code STRICT} mode because it does not depend on era information.</p>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @since JDK 8
 * @see AgeCalculator
 * @see DateUtils
 */
public class DateValidator {

    /**
     * Date formatter configured with the pattern {@code dd/MM/uuuu} and
     * {@link ResolverStyle#STRICT} to ensure calendar validity.
     *
     * <p>The proleptic year field {@code uuuu} is used instead of {@code yyyy}
     * because {@code ResolverStyle.STRICT} requires it — see the class-level
     * documentation for details.</p>
     */
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Private constructor prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always, since this is a utility class
     */
    private DateValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Parses a date string in {@code DD/MM/YYYY} format and returns a
     * {@link java.time.LocalDate} object.
     *
     * <p>Uses {@link DateTimeFormatter#ofPattern(String)} with the pattern
     * {@code dd/MM/uuuu} and {@link ResolverStyle#STRICT} to ensure calendar
     * validity — for example, February 30 and February 29 in non-leap years
     * are both rejected.</p>
     *
     * <p><strong>Example — Normal usage:</strong></p>
     * <pre>{@code
     * LocalDate date = DateValidator.parseDate("15/08/1998");
     * // Returns LocalDate representing August 15, 1998
     * }</pre>
     *
     * <p><strong>Example — Invalid calendar date:</strong></p>
     * <pre>{@code
     * LocalDate date = DateValidator.parseDate("31/02/2020");
     * // Throws DateTimeParseException — February 31 does not exist
     * }</pre>
     *
     * @param input the date string in DD/MM/YYYY format (e.g., {@code "15/08/1998"})
     * @return the parsed {@link LocalDate} object representing the user's Date of Birth
     * @throws DateTimeParseException if the input does not match the DD/MM/YYYY format
     *         or represents an invalid calendar date (e.g., {@code "31/02/2020"})
     * @throws IllegalArgumentException if {@code input} is {@code null} or empty
     */
    public static LocalDate parseDate(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
        return LocalDate.parse(input.trim(), FORMATTER);
    }

    /**
     * Validates whether a given string represents a valid date in {@code DD/MM/YYYY} format.
     *
     * <p>Returns {@code true} if the string can be successfully parsed into a valid
     * calendar date, {@code false} otherwise. This method internally attempts to call
     * {@link #parseDate(String)} and catches any exceptions, converting them to a
     * {@code false} return value.</p>
     *
     * <p>This method does <strong>NOT</strong> check whether the date is in the future —
     * use {@link #isFutureDate(LocalDate)} for temporal validation.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * boolean valid1 = DateValidator.isValidDate("15/08/1998"); // true
     * boolean valid2 = DateValidator.isValidDate("31/02/2020"); // false
     * boolean valid3 = DateValidator.isValidDate("hello");      // false
     * }</pre>
     *
     * @param input the date string to validate
     * @return {@code true} if the input is a valid date in DD/MM/YYYY format,
     *         {@code false} otherwise
     */
    public static boolean isValidDate(String input) {
        try {
            parseDate(input);
            return true;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks whether the given date is <strong>after</strong> the current date
     * ({@link LocalDate#now()}).
     *
     * <p>Returns {@code true} if the date is in the future, {@code false} if it
     * is today or in the past. This method is used to reject future dates as
     * Date of Birth — a person cannot have been born on a date that has not yet
     * occurred.</p>
     *
     * <p><strong>Boundary behavior:</strong> Today's date is considered
     * <strong>not</strong> future. A newborn baby born today has a valid Date
     * of Birth equal to today's date.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * boolean future1 = DateValidator.isFutureDate(LocalDate.of(1998, 8, 15)); // false
     * boolean future2 = DateValidator.isFutureDate(LocalDate.of(2030, 12, 25)); // true
     * boolean future3 = DateValidator.isFutureDate(LocalDate.now());            // false
     * }</pre>
     *
     * @param date the date to check against the current date ({@link LocalDate#now()})
     * @return {@code true} if the date is after today, {@code false} otherwise
     * @throws IllegalArgumentException if {@code date} is {@code null}
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }
        return date.isAfter(LocalDate.now());
    }
}
