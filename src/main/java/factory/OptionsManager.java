package factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Builds browser-specific {@link Options} instances from framework properties.
 * <p>
 * Supported properties: {@code headless} and {@code incognito}.
 */
public class OptionsManager {

    private Properties properties;
    private ChromeOptions chromeOptions;
    private FirefoxOptions firefoxOptions;
    private EdgeOptions edgeOptions;

    private static final Logger log = LoggerFactory.getLogger(OptionsManager.class);

    /**
     * Creates a new OptionsManager backed by the supplied properties.
     *
     * @param properties environment configuration
     */
    public OptionsManager(Properties properties) {
        this.properties = properties;
    }

    /**
     * Builds ChromeOptions honoring {@code headless} and {@code incognito}.
     *
     * @return configured ChromeOptions
     */
    public ChromeOptions getChromeOptions() {
        chromeOptions = new ChromeOptions();
        if (Boolean.parseBoolean(properties.getProperty("headless"))) {
            log.info("===Running in Headless Mode in Chrome Browser ===");
            System.out.println("===Running in Headless Mode===");

            chromeOptions.addArguments("--headless");
        }
        if (Boolean.parseBoolean(properties.getProperty("incognito"))) {
            log.info("===Running in Incognito Mode===");
            System.out.println("===Running in Incognito Mode in Chrome Browser ===");
            chromeOptions.addArguments("--incognito");
        }

        return chromeOptions;

    }

    /**
     * Builds FirefoxOptions honoring {@code headless} and {@code incognito}.
     *
     * @return configured FirefoxOptions
     */
    public FirefoxOptions getFireFoxOptions() {
        firefoxOptions = new FirefoxOptions();
        if (Boolean.parseBoolean(properties.getProperty("headless"))) {
            System.out.println("===Running in Headless Mode===");
            log.info("===Running in Headless Mode in Firefox Browser ===");
            firefoxOptions.addArguments("--headless");
        }
        if (Boolean.parseBoolean(properties.getProperty("incognito"))) {
            System.out.println("===Running in Incognito Mode===");
            log.info("===Running in Incognito Mode in Firefox Browser===");
            firefoxOptions.addArguments("--incognito");
        }
        return firefoxOptions;
    }

    /**
     * Builds EdgeOptions honoring {@code headless} and {@code incognito}.
     *
     * @return configured EdgeOptions
     */
    public EdgeOptions getEdgeBrowserOptions() {
        edgeOptions = new EdgeOptions();
        if (Boolean.parseBoolean(properties.getProperty("headless"))) {
            System.out.println("===Running in Headless Mode===");
            log.info("===Running in Headless Mode in Edge Browser ===");
            edgeOptions.addArguments("--headless");
        }
        if (Boolean.parseBoolean(properties.getProperty("incognito"))) {
            System.out.println("===Running in Incognito Mode===");
            log.info("===Running in Incognito Mode in Edge Browser ===");
            edgeOptions.addArguments("--inPrivate");
        }
        return edgeOptions;
    }
}
