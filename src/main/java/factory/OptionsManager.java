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

public class OptionsManager {

    private Properties properties;
    private ChromeOptions chromeOptions;
    private FirefoxOptions firefoxOptions;
    private EdgeOptions edgeOptions;

    private static final Logger log = LoggerFactory.getLogger(OptionsManager.class);

    public OptionsManager(Properties properties) {
        this.properties = properties;
    }

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
