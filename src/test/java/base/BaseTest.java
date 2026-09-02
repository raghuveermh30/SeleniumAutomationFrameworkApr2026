package base;

import com.aventstack.chaintest.plugins.ChainTestListener;
import factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.util.Properties;


/**
 * Base class for all TestNG test classes.
 * <p>
 * Handles WebDriver setup/teardown and exposes shared page object fields
 * (e.g. {@link LoginPage}, {@link HomePage}) so subclasses can navigate the
 * application without instantiating pages directly.
 */
public class BaseTest {

    WebDriver driver;
    DriverFactory driverFactory;
    protected Properties properties;

    protected LoginPage loginPage;
    protected HomePage homePage;
    protected SearchResultsPage searchResultsPage;
    protected ProductInfoPage productInfoPage;
    protected LogoutPage logoutPage;
    protected CommonPage commonPage;
    protected RegistrationPage registrationPage;

    /**
     * Initializes the WebDriver and loads environment properties before each test.
     *
     * @param browserName the browser injected from the TestNG XML parameter;
     *                    falls back to the value in config.properties when not supplied
     */
    @Parameters({"browser"})
    @BeforeTest
    public void setup(@Optional String browserName) {

        driverFactory = new DriverFactory();
        properties = driverFactory.initProp();

        if (browserName!= null){
            properties.setProperty("browser", browserName); //Passing the Browser Value from XML
        }

        driver = driverFactory.initDriver(properties);

        commonPage = new CommonPage(driver);
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);

        // ChainPluginService.getInstance().addSystemInfo("Build#", "1.0");
        // ChainPluginService.getInstance().addSystemInfo("Headless#", properties.getProperty("headless"));
        // ChainPluginService.getInstance().addSystemInfo("Incognito#", properties.getProperty("incognito"));
        // ChainPluginService.getInstance().addSystemInfo("Author#", "Naveen Automation Labs");

    }

    /**
     * Attaches a screenshot to the ChainTest report when a test fails.
     *
     * @param result the result of the test method that just ran
     */
    @AfterMethod
    public void attachScreenshotOnFailure(ITestResult result) {
        if (!result.isSuccess()) {
            ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
            // ChainTestListener.embed(DriverFactory.getScreenshotByte(), "image/png");
            //ChainTestListener.embed(DriverFactory.getScreenshotBase64(), "image/png");
        }
    }

    /**
     * Quits the WebDriver instance after the test suite finishes.
     */
    @AfterTest(alwaysRun = true, description = "Teardown Method: Close the browser and clean up resources")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
