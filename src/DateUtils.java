import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Reusable utility class providing additional age-related calculations.
 *
 * <p>{@code DateUtils} offers convenience methods for computing age in different
 * units (years/months/days breakdown, total months, total days), finding the next
 * birthday date, and counting down the days until the next birthday. All methods
 * are {@code public static}, requiring no instance creation.</p>
 *
 * <p>This class follows the standard Java utility class pattern (similar to
 * {@link java.util.Collections} and {@link java.util.Arrays}):</p>
 * <ul>
 *   <li>All methods are {@code public static}</li>
 *   <li>Private constructor prevents accidental instantiation</li>
 *   <li>Thread-safe by design — uses only local variables and immutable
 *       {@code java.time} classes</li>
 *   <li>No external dependencies — uses only the Java Standard Library</li>
 * </ul>
 *
 * <p><strong>Note:</strong> This class is an optional enhancement. The core Age
 * Calculator application ({@link AgeCalculator} + {@link DateValidator}) functions
 * completely without it.</p>
 *
 * @author Age Calculator Project
 * @version 1.0
 * @since JDK 8
 * @see AgeCalculator
 * @see DateValidator
 */
public class DateUtils {

    /**
     * Private constructor prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always, since this is a utility class
     */
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Calculates the age from the given birth date to the current date and returns
     * a formatted string.
     *
     * <p>This is a simplified convenience wrapper around
     * {@link Period#between(LocalDate, LocalDate)}. The method internally obtains
     * the current date via {@link LocalDate#now()}, computes the {@code Period},
     * and formats the result.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = LocalDate.of(1998, 8, 15);
     * String age = DateUtils.calculateAge(dob);
     * System.out.println("Your age is " + age + ".");
     * // Output: "Your age is 27 years, 6 months, and 15 days." (varies by current date)
     * }</pre>
     *
     * @param birthDate the date of birth as a {@link LocalDate} instance
     * @return formatted age string in the pattern {@code X years, Y months, and Z days}
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or in the future
     */
    public static String calculateAge(LocalDate birthDate) {
        validateBirthDate(birthDate);
        Period age = Period.between(birthDate, LocalDate.now());
        return String.format("%d years, %d months, and %d days",
            age.getYears(), age.getMonths(), age.getDays());
    }

    /**
     * Calculates the total number of complete months from the birth date to the
     * current date using {@link ChronoUnit#MONTHS}.
     *
     * <p>Unlike {@link #calculateAge(LocalDate)} which breaks the age into years,
     * months, and days, this method returns a single {@code long} value representing
     * the <strong>total</strong> elapsed months. For example, an age of 2 years and
     * 3 months returns {@code 27} (not {@code 3}).</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = LocalDate.of(1998, 8, 15);
     * long months = DateUtils.totalMonths(dob);
     * System.out.println("Total months: " + months);
     * }</pre>
     *
     * @param birthDate the date of birth as a {@link LocalDate} instance
     * @return total number of complete months since birth as a {@code long} value
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or in the future
     */
    public static long totalMonths(LocalDate birthDate) {
        validateBirthDate(birthDate);
        return ChronoUnit.MONTHS.between(birthDate, LocalDate.now());
    }

    /**
     * Calculates the total number of days from the birth date to the current date
     * using {@link ChronoUnit#DAYS}.
     *
     * <p>This method provides the most granular age measurement, counting every
     * single day between the birth date and today. Leap years are accounted for
     * automatically.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = LocalDate.of(1998, 8, 15);
     * long days = DateUtils.totalDays(dob);
     * System.out.println("Total days: " + days);
     * }</pre>
     *
     * @param birthDate the date of birth as a {@link LocalDate} instance
     * @return total number of days since birth as a {@code long} value
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or in the future
     */
    public static long totalDays(LocalDate birthDate) {
        validateBirthDate(birthDate);
        return ChronoUnit.DAYS.between(birthDate, LocalDate.now());
    }

    /**
     * Calculates the date of the next upcoming birthday.
     *
     * <p>If today is the person's birthday, this method returns next year's
     * birthday date (the "next" birthday is always strictly in the future).</p>
     *
     * <p><strong>Leap Year Handling:</strong> For persons born on February 29
     * (a leap year date), the birthday falls on March 1 in non-leap years
     * (Approach A — treats the birthday as the day after February 28).</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = LocalDate.of(1998, 8, 15);
     * LocalDate next = DateUtils.nextBirthday(dob);
     * System.out.println("Next birthday: " + next);
     * }</pre>
     *
     * @param birthDate the date of birth as a {@link LocalDate} instance
     * @return the date of the next birthday as a {@link LocalDate}
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or in the future
     */
    public static LocalDate nextBirthday(LocalDate birthDate) {
        validateBirthDate(birthDate);
        LocalDate today = LocalDate.now();
        LocalDate nextBday = birthDate.withYear(today.getYear());

        // Handle February 29 birthdays in non-leap years (Approach A: use March 1)
        if (birthDate.getMonthValue() == 2 && birthDate.getDayOfMonth() == 29
                && !today.isLeapYear()) {
            nextBday = LocalDate.of(today.getYear(), 3, 1);
        }

        // If the birthday this year has already passed or is today, advance to next year
        if (nextBday.isBefore(today) || nextBday.isEqual(today)) {
            nextBday = nextBday.plusYears(1);
            // Re-check leap year handling for the next year
            if (birthDate.getMonthValue() == 2 && birthDate.getDayOfMonth() == 29
                    && !nextBday.isLeapYear()) {
                nextBday = LocalDate.of(nextBday.getYear(), 3, 1);
            }
        }

        return nextBday;
    }

    /**
     * Calculates the number of days remaining until the next birthday.
     *
     * <p>Combines {@link #nextBirthday(LocalDate)} with
     * {@link ChronoUnit#DAYS} to compute the countdown. Since
     * {@code nextBirthday()} always returns a strictly future date, the
     * minimum return value is {@code 1}.</p>
     *
     * <p><strong>Example:</strong></p>
     * <pre>{@code
     * LocalDate dob = LocalDate.of(1998, 8, 15);
     * long days = DateUtils.daysUntilNextBirthday(dob);
     * System.out.println("Days until next birthday: " + days);
     * }</pre>
     *
     * @param birthDate the date of birth as a {@link LocalDate} instance
     * @return the number of days until the next birthday as a {@code long} value (1–366)
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or in the future
     */
    public static long daysUntilNextBirthday(LocalDate birthDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), nextBirthday(birthDate));
    }

    /**
     * Validates the birth date parameter used by all public methods.
     *
     * <p>Ensures the birth date is not {@code null} and not in the future.
     * This centralized validation avoids code duplication across methods.</p>
     *
     * @param birthDate the birth date to validate
     * @throws IllegalArgumentException if {@code birthDate} is {@code null} or
     *         after {@link LocalDate#now()}
     */
    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date must not be null.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future.");
        }
    }
}
