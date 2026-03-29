package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public class JavaScriptExecutorUtil {

    private WebDriver driver;
    private JavascriptExecutor js;

    public JavaScriptExecutorUtil(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
    }

    public String getPageTitleUsingJs() {
        return Objects.requireNonNull(js.executeScript("return document.title;")).toString();
    }

    public String getPageUrlUsingJs() {
        return Objects.requireNonNull(js.executeScript("return document.URL;")).toString();
    }

    //Browser Navigation methods
    public void refreshBrowserByJS() {
        js.executeScript("history.go(0)");
    }

    public void navigateToBackPage() {
        js.executeScript("history.go(-1)");
    }

    public void navigateToForwardPage() {
        js.executeScript("history.go(1)");
    }

    //Generate the Alerts
    public void generateJSAlert(String message) {
        js.executeScript("alert('" + message + "')");
    }

    public String getPageInnerText() {
        return js.executeScript("return document.documentElement.innerText;").toString();
    }

    //Scrolling Methods
    public void scrollPageDown() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollPageUp() {
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }

    public void scrollPageDown(String height) {
        js.executeScript("window.scrollTo(0, '" + height + "')");
    }

    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //Draw Border
    public void drawBorder(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    //Element Highlight
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
    
    public void clickElementByJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    public void sendKeysByJSUsingId(String id, String value) {
        js.executeScript("document.getElementById('" + id + "').value='" + value + "'");
    }

}


