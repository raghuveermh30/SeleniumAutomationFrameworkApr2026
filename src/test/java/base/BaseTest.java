package base;

import com.aventstack.chaintest.plugins.ChainTestListener;
import factory.DriverFactory;
import jdk.jfr.Description;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.util.Properties;


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

    @Parameters({"browser"})
    @Description("Initialize the Driver and Properties")
    @BeforeTest
    public void setup(String browserName) {

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

    @Description("Taking the Screenshot only if test case is failed")
    @AfterMethod
    public void attachScreenshotOnFailure(ITestResult result) {
        if (!result.isSuccess()) {
            ChainTestListener.embed(DriverFactory.getScreenshotFile(), "image/png");
            // ChainTestListener.embed(DriverFactory.getScreenshotByte(), "image/png");
            //ChainTestListener.embed(DriverFactory.getScreenshotBase64(), "image/png");
        }
    }

    @Description("Closing the Browser")
    @AfterTest(alwaysRun = true, description = "Teardown Method: Close the browser and clean up resources")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
