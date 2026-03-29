package factory;

import constants.AppConstants;
import errors.FrameworkException;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class DriverFactory {

    WebDriver driver;
    Properties properties;
    OptionsManager optionsManager;
    public static String highlight;

    public static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    //Thread Local means when ever we are generating the thread, we will be having the local copy of the driver.

    /* Advantage of Thread Local
     * When we have bulk test cases in parallel mode then better to use ThreadLocal concept
     * Thread local driver means give me the local copy of the driver. Every thread will get the local copy of the driver
     * Here we won;t get unnecessary wait exception on the driver since each driver will be having the copy.
     * We can achieve proper Thread Management in the framework design
     *
     * When we have 300-400 test cases in parallel mode then there is high chance of getting driver clash error. This will avoid
     * When report is not coming properly and there are high chances of getting overridden when its in parallel mode execution
     * ThreadLocal is the generic, we can use it for anything, Here we are using for WebDriver
     * thread-count = 3, Here every thread will get the WebDriver local copy. Every thread are running independent
     * Here, we won't get DeadLock exception
     * We need to implement when we test cases will be executed in the parallel mode execution
     */

    @Step("init the driver using properties : {0}")
    public WebDriver initDriver(Properties properties) {

        String browserName = properties.getProperty("browser");

        highlight = properties.getProperty("highlight");

        log.info("Browser Name is : " + browserName);
        optionsManager = new OptionsManager(properties);
        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                log.debug("Running the test cases in Chrome Browser");
                driverThreadLocal.set(new ChromeDriver(optionsManager.getChromeOptions()));
                //driver = new ChromeDriver(optionsManager.getChromeOptions());
                break;
            case "firefox":
                log.debug("Running the test cases in firefox Browser");
                driverThreadLocal.set(new FirefoxDriver(optionsManager.getFireFoxOptions()));
                //driver = new FirefoxDriver(optionsManager.getFireFoxOptions());
                break;
            case "edge":
                log.debug("Running the test cases in edge Browser");
                driverThreadLocal.set(new EdgeDriver(optionsManager.getEdgeBrowserOptions()));
                //driver = new EdgeDriver(optionsManager.getEdgeBrowserOptions());
                break;
            case "safari":
                log.debug("Running the test cases in safari Browser");
                driver = new SafariDriver();
                break;
            default:
                log.error("Please pass the correct Browser " + browserName);
                throw new FrameworkException("====Invalid Browser Name===");
        }
        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        getDriver().get(properties.getProperty("url"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(AppConstants.DEFAULT_TIME_OUT));
        return getDriver();
    }


    /* This method is used to read the properties from .properties file
     * This is used to initialize the properties for multiple environments
     * We are using Maven command line to pass the environment
     * @return
     */
    //supply the environment name from maven command line
    //mvn clean install -Denv="qa"
    //If any one is not passing the environment from the command line, then by default it will pick up the qa environment

    @Step("init the  properties")
    public Properties initProp() {
        properties = new Properties();
        FileInputStream fileInputStream = null;
        String envName = System.getProperty("env");
        log.info("Running test cases on environment: {}", envName);
        try {
            if (envName == null || envName.trim().isEmpty()) {
                log.info("No env is passed, hence Running test suite on PROD Environment");
                fileInputStream = new FileInputStream(AppConstants.CONFIG_PROD_PROP_FILE_PATH);
            } else {
                switch (envName.toLowerCase().trim()) {
                    case "qa":
                        log.info("Running test suite on QA Environment");
                        fileInputStream = new FileInputStream(AppConstants.CONFIG_QA_PROP_FILE_PATH);
                        break;
                    case "uat":
                        log.info("Running test suite on UAT Environment");
                        fileInputStream = new FileInputStream(AppConstants.CONFIG_UAT_PROP_FILE_PATH);
                        break;
                    case "stage":
                        log.info("Running test suite on STAGE Environment");
                        fileInputStream = new FileInputStream(AppConstants.CONFIG_STAGE_PROP_FILE_PATH);
                        break;
                    default:
                        throw new FrameworkException("Invalid Environment : " + envName);
                }
            }

            properties.load(fileInputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    /* This is used to get the driver with ThreadLocal
     *
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }


    /* Logger has 5 levels - DEBUG, INFO, WARN, ERROR, FATAL
     * Debug - Detailed information, typically of interest only when diagnosing problems.
     * Info - Confirmation that things are working as expected.
     * Warn - An indication that something unexpected happened, or indicative of some problem in the near future (e.g. 'disk space low'). The software is still working as expected.
     * Error - Due to a more serious problem, the software has not been able to perform some function.
     * Fatal - Severe errors that cause premature termination. Expect these to be immediately visible on a console and in logs.
     */

    /*
     * takescreenshot
     */
    @Step("getScreenshot")
    public static String getScreenShot() {
        log.info(">>> Take ScreenShot");
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/screenshot/" + "_" + System.currentTimeMillis() + ".png";
        File destinationFile = new File(path);
        try {
            FileHandler.copy(srcFile, destinationFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    @Step("getScreenshotFile")
    public static File getScreenshotFile() {
        log.info(">>>getScreenshotFile()");
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// temp dir
        return srcFile;
    }

    @Step("getScreenshotByte")
    public static byte[] getScreenshotByte() {
        log.info(">>>getScreenshotByte()");
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);// temp dir

    }

    @Step("getScreenshotBase64")
    public static String getScreenshotBase64() {
        log.info(">>>getScreenshotBase64()");
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);// temp dir

    }

}
