package utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generic string helpers used across the framework for generating unique
 * test data, normalizing strings and other reusable text operations.
 */
public final class StringUtil {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private StringUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Returns the current timestamp formatted as {@code yyyyMMddHHmmss}.
     *
     * @return a timestamp string
     */
    public static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * Generates a random string from the given character set.
     *
     * @param source  the character pool to pick from
     * @param length  the desired length
     * @return a randomly generated string
     */
    private static String getRandomString(String source, int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(source.charAt(RANDOM.nextInt(source.length())));
        }
        return builder.toString();
    }

    /**
     * Generates a random numeric string of the requested length.
     *
     * @param length the number of digits to generate
     * @return a numeric string
     */
    public static String getRandomNumber(int length) {
        return getRandomString(NUMERIC, length);
    }

    /**
     * Generates a random alphanumeric string of the requested length.
     *
     * @param length the number of characters to generate
     * @return an alphanumeric string
     */
    public static String getRandomAlphanumeric(int length) {
        return getRandomString(ALPHANUMERIC, length);
    }

    /**
     * Creates a unique email address by prefixing the supplied base email
     * with a timestamp and a random number.
     * <p>
     * Example: {@code test@gmail.com} becomes {@code 20240902143015_1234_test@gmail.com}
     *
     * @param email the base email address
     * @return a unique email address
     */
    public static String getUniqueEmail(String email) {
        return getTimestamp() + "_" + getRandomNumber(4) + "_" + email;
    }
}
