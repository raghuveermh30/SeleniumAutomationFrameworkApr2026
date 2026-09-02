package listeners;


import factory.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


/**
 * TestNG listener that integrates test lifecycle events with Allure reporting,
 * attaching screenshots and logs and printing lifecycle events to the console.
 */
public class TestAllureListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }


    /**
     * Captures a screenshot of the given driver and attaches it to the Allure report.
     *
     * @param driver the WebDriver to capture the screenshot from
     * @return the screenshot as a PNG byte array
     */
    // Text attachments for Allure
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Attaches a plain text log message to the Allure report.
     *
     * @param message the message to attach
     * @return the same message, as required by the {@link Attachment} contract
     */
    // Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    /**
     * Attaches an HTML snippet to the Allure report.
     *
     * @param html the HTML content to attach
     * @return the same HTML content, as required by the {@link Attachment} contract
     */
    // HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    /**
     * Invoked when a test context starts execution.
     *
     * @param iTestContext the TestNG test context
     */
    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in onStart method " + iTestContext.getName());
        //iTestContext.setAttribute("WebDriver", BasePage.getDriver());
    }

    /**
     * Invoked when a test context finishes execution.
     *
     * @param iTestContext the TestNG test context
     */
    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
    }

    /**
     * Invoked when an individual test method starts.
     *
     * @param iTestResult the result object for the starting test
     */
    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
    }

    /**
     * Invoked when an individual test method passes.
     *
     * @param iTestResult the result object for the successful test
     */
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
    }

    /**
     * Invoked when an individual test method fails; captures a screenshot and log for Allure.
     *
     * @param iTestResult the result object for the failed test
     */
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        Object testClass = iTestResult.getInstance();
        //WebDriver driver = BasePage.getDriver();
        // Allure ScreenShotRobot and SaveTestLog
        if (DriverFactory.getDriver() instanceof WebDriver) {
            System.out.println("Screenshot captured for test case:" + getTestMethodName(iTestResult));
            saveScreenshotPNG(DriverFactory.getDriver());
        }
        // Save a log on allure.
        saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
    }

    /**
     * Invoked when an individual test method is skipped.
     *
     * @param iTestResult the result object for the skipped test
     */
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
    }

    /**
     * Invoked when a test fails but is still within the configured success percentage.
     *
     * @param iTestResult the result object for the test
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

}