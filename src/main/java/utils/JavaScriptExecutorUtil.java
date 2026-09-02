package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

/**
 * Utility for performing JavaScript-based browser interactions via
 * {@link JavascriptExecutor}, such as scrolling, highlighting and navigation.
 */
public class JavaScriptExecutorUtil {

    private WebDriver driver;
    private JavascriptExecutor js;

    /**
     * Creates a new JavaScriptExecutorUtil bound to the given driver.
     *
     * @param driver the WebDriver instance to operate on
     */
    public JavaScriptExecutorUtil(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
    }

    /**
     * Returns the page title read via JavaScript.
     *
     * @return the document title
     */
    public String getPageTitleUsingJs() {
        return Objects.requireNonNull(js.executeScript("return document.title;")).toString();
    }

    /**
     * Returns the page URL read via JavaScript.
     *
     * @return the document URL
     */
    public String getPageUrlUsingJs() {
        return Objects.requireNonNull(js.executeScript("return document.URL;")).toString();
    }

    //Browser Navigation methods
    /**
     * Refreshes the current page using {@code history.go(0)}.
     */
    public void refreshBrowserByJS() {
        js.executeScript("history.go(0)");
    }

    /**
     * Navigates back one page using {@code history.go(-1)}.
     */
    public void navigateToBackPage() {
        js.executeScript("history.go(-1)");
    }

    /**
     * Navigates forward one page using {@code history.go(1)}.
     */
    public void navigateToForwardPage() {
        js.executeScript("history.go(1)");
    }

    //Generate the Alerts
    /**
     * Triggers a browser {@code alert()} with the given message.
     *
     * @param message the message to display in the alert
     */
    public void generateJSAlert(String message) {
        js.executeScript("alert('" + message + "')");
    }

    /**
     * Returns the page's rendered inner text.
     *
     * @return the {@code innerText} of the document element
     */
    public String getPageInnerText() {
        return js.executeScript("return document.documentElement.innerText;").toString();
    }

    //Scrolling Methods
    /**
     * Scrolls the page to the bottom.
     */
    public void scrollPageDown() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Scrolls the page to the top.
     */
    public void scrollPageUp() {
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    /**
     * Scrolls the page to the given vertical height.
     *
     * @param height the target scroll height
     */
    public void scrollPageDown(String height) {
        js.executeScript("window.scrollTo(0, '" + height + "')");
    }

    /**
     * Scrolls the given element into view.
     *
     * @param element the WebElement to scroll into view
     */
    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //Draw Border
    /**
     * Draws a red border around the given element for visual debugging.
     *
     * @param element the WebElement to outline
     */
    public void drawBorder(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    //Element Highlight
    /**
     * Flashes the given element's background color to visually highlight it.
     *
     * @param element the WebElement to highlight
     */
    public void flash(WebElement element) {
        String bgcolor = element.getCssValue("backgroundColor");//Grey
        for (int i = 0; i < 10; i++) {
            changeColor("rgb(0,200,0)", element);//Green
            changeColor(bgcolor, element);//Grey
        }
    }

    private void changeColor(String color, WebElement element) {
        js.executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
        try {
            Thread.sleep(20);
        } catch (InterruptedException ignored) {
        }
    }
    
    /**
     * Clicks the given element via JavaScript instead of a native Selenium click.
     *
     * @param element the WebElement to click
     */
    public void clickElementByJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Sets the value of an input element identified by its id, via JavaScript.
     *
     * @param id the id attribute of the target element
     * @param value the value to set
     */
    public void sendKeysByJSUsingId(String id, String value) {
        js.executeScript("document.getElementById('" + id + "').value='" + value + "'");
    }

}


