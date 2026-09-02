package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

/**
 * Generic browser-level utilities that operate on the WebDriver instance.
 * <p>
 * These helpers are page-agnostic and can be reused across any page object
 * that needs to control windows, tabs, navigation or page-level waits.
 */
public final class BrowserUtil {

    private final WebDriver driver;

    /**
     * Creates a new BrowserUtil bound to the given driver.
     *
     * @param driver the WebDriver instance to operate on
     */
    public BrowserUtil(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits until the browser's {@code document.readyState} equals "complete".
     *
     * @param timeoutInSeconds max time to wait for the page to be fully loaded
     */
    public void waitForPageLoad(long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Refreshes the current browser page.
     */
    public void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Navigates the browser back one step in its history.
     */
    public void navigateBack() {
        driver.navigate().back();
    }

    /**
     * Navigates the browser forward one step in its history.
     */
    public void navigateForward() {
        driver.navigate().forward();
    }

    /**
     * Navigates to the given URL.
     *
     * @param url the destination URL
     */
    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    /**
     * Returns the currently active window / tab handle.
     *
     * @return the active window handle
     */
    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Returns all open window / tab handles.
     *
     * @return a set of window handles
     */
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Switches to the first window whose page title contains the given text.
     *
     * @param titleFraction a substring of the target window title
     * @return the handle of the switched-to window
     * @throws RuntimeException if no matching window is found
     */
    public String switchToWindowByTitle(String titleFraction) {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(titleFraction)) {
                return handle;
            }
        }
        // Restore original window if not found to avoid losing context
        driver.switchTo().window(currentHandle);
        throw new RuntimeException("No window found with title containing: " + titleFraction);
    }

    /**
     * Switches to the first window whose URL contains the given text.
     *
     * @param urlFraction a substring of the target window URL
     * @return the handle of the switched-to window
     * @throws RuntimeException if no matching window is found
     */
    public String switchToWindowByUrl(String urlFraction) {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getCurrentUrl().contains(urlFraction)) {
                return handle;
            }
        }
        driver.switchTo().window(currentHandle);
        throw new RuntimeException("No window found with URL containing: " + urlFraction);
    }

    /**
     * Closes all windows/tabs except the one identified by the given handle,
     * then switches back to the remaining window.
     *
     * @param keepWindowHandle the handle of the window that should remain open
     */
    public void closeAllTabsExcept(String keepWindowHandle) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(keepWindowHandle)) {
                driver.switchTo().window(handle).close();
            }
        }
        driver.switchTo().window(keepWindowHandle);
    }

    /**
     * Maximizes the current browser window.
     */
    public void maximizeWindow() {
        driver.manage().window().maximize();
    }
}
