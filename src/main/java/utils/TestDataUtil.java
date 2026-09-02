package utils;

import org.testng.annotations.DataProvider;

/**
 * Reusable TestNG {@link DataProvider}s shared across multiple test classes.
 * <p>
 * Keeping common data sets in one place avoids duplication and makes test data
 * easier to maintain as the application evolves.
 */
public final class TestDataUtil {

    private TestDataUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Supplies the list of footer links that should be present on the OpenCart pages.
     *
     * @return an array of footer link names
     */
    @DataProvider(name = "footerLinksData")
    public static Object[][] getFooterLinksData() {
        return new Object[][]{
                {"Contact Us"},
                {"Delivery Information"},
                {"Returns"},
                {"Brands"},
                {"Gift Certificates"},
                {"Affiliate"},
                {"Specials"},
                {"My Account"}
        };
    }
}
