package utils;


import constants.AppConstants;
import factory.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import utils.JavaScriptExecutorUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class ElementUtil {

    private final WebDriver driver;
    private JavaScriptExecutorUtil javaScriptExecutorUtil;

    public ElementUtil(WebDriver driver) {
        this.driver = driver;
        javaScriptExecutorUtil = new JavaScriptExecutorUtil(driver);
    }

    private void highlightElement(WebElement element) {
        if (Boolean.parseBoolean(DriverFactory.highlight)) {
            javaScriptExecutorUtil.flash(element);
        }
    }

    public WebElement getElement(By locator) {
        //waitForElementVisible(locator, 10); //default wait is 10 seconds
        //waitForElementVisible -> this method will be auto wait feature and it will wait for all the elements unnecessarily.
        //The above method will slow down the performance of the script.

        WebElement element = driver.findElement(locator);
        highlightElement(element);
        return element;
    }

    @Step("Fill the values for Webelement: {0} and the value is: {1}")
    public void doSendKeys(By locator, CharSequence... value) {
        nullCheck(value);
        WebElement element = getElement(locator);
        waitForElementsToBePresent(locator, AppConstants.MEDIUM_TIME_OUT);
        element.clear();
        delay(1000);
        element.sendKeys(value);
    }

    @Step("Fill the values for Webelement: {0} and the value is: {1}")
    public void doSendKeys(String locatorType, String locatorValue, CharSequence... value) {
        nullCheck(value);
        WebElement element = getElement(getLocator(locatorType, locatorValue));
        element.clear();
        element.sendKeys(value);
    }

    @Step("Fill the values for Webelement: {0} and the value is: {1}")
    public void doSendKeys(WebElement element, CharSequence... value) {
        nullCheck(value);
        element.clear();
        element.sendKeys(value);
    }

    private void nullCheck(CharSequence... value) {
        if (value == null) {
            throw new RuntimeException("===VALUE/PROP/ATTRIBUTE CANNOT BE NULL===");
        }
    }

    private void delay(long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Click the Webelement")
    public void doClick(By locator) {
        getElement(locator).click();
    }

    @Step("Clicking on the WebElement {0}")
    public void doClick(String locatorType, String locatorValue) {
        getElement(getLocator(locatorType, locatorValue)).click();
    }

    @Step("Get the Element Text on the WebElement: {0}")
    public String doElementGetText(By locator) {
        // waitForElementVisible(locator, AppConstants.DEFAULT_TIME_OUT);
        waitForElementsToVisible(locator, AppConstants.DEFAULT_TIME_OUT);
        return getElement(locator).getText();
    }

    public String getElementText(String locatorType, String locatorValue) {
        return getElement(getLocator(locatorType, locatorValue)).getText();
    }

    @Step("get the locator")
    public static By getLocator(String locatorType, String locatorValue) {

        By locator = null;
        switch (locatorType.toUpperCase().trim()) {
            case "ID":
                locator = By.id(locatorValue);
                break;
            case "NAME":
                locator = By.name(locatorValue);
                break;
            case "CLASS NAME":
                locator = By.className(locatorValue);
                break;
            case "XPATH":
                locator = By.xpath(locatorValue);
                break;
            case "CSS":
                locator = By.cssSelector(locatorValue);
                break;
            case "LINK TEXT":
                locator = By.linkText(locatorValue);
                break;
            case "PARTIAL LINK TEXT":
                locator = By.partialLinkText(locatorValue);
                break;
            case "TAG NAME":
                locator = By.tagName(locatorValue);
                break;
            default:
                throw new IllegalArgumentException("==INVALID LOCATOR, PLEASE USE CORRECT LOCATOR==");
        }
        return locator;
    }


    @Step("get list of WebElements using the locator {0}")
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    @Step("check element is displayed using the locator {0}")
    public boolean isElementDisplayed(By locator) {
        if (getElements(locator).size() == 1) {
            System.out.println("Element is available one time on the page ");
            return true;
        } else {
            System.out.println("Element is not available one time on the page ");
            return false;
        }
    }

    @Step("check element is displayed using the locator {0} with the count {1}")
    public boolean isElementDisplayed(By locator, int eleCount) {
        if (getElements(locator).size() == eleCount) {
            System.out.println(locator + "Element is available " + eleCount + " times on the page ");
            return true;
        } else {
            System.out.println(locator + "Element is not available " + eleCount + " times on the page ");
            return false;
        }
    }

    @Step("get Webelement using the locator {0}")
    public WebElement getFindElement(By locator) {
        return driver.findElement(locator);
    }

    @Step("element {0} is displayed..")
    public boolean doElementIsDisplayed(By locator) {
        try {
            return getFindElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Element is not displayed");
            return false;
        }
    }

    @Step("get the count of elements on the page using the locator {0}")
    public String doGetDomAttribute(By locator, String attributeName) {
        nullCheck(attributeName);
        return getElement(locator).getDomAttribute(attributeName);
    }

    @Step("get the count of elements on the page using the locator {0}")
    public String doGetDomProperty(By locator, String propName) {
        nullCheck(propName);
        return getElement(locator).getDomProperty(propName);
    }

    @Step("get the count of elements on the page using the locator {0}")
    public List<String> fetchElementsText(By locator) {
        List<String> eleTextList = new ArrayList<>();
        getElements(locator).stream().filter(e -> !e.getText().isEmpty()).forEach(e -> eleTextList.add(e.getText()));
        return eleTextList;
    }

    @Step("get the count of elements on the page using the locator {0}")
    public void getElementsText(By locator) {
        getElements(locator).stream().filter(e -> !e.getText().isEmpty()).forEach(e -> System.out.println(e.getText()));
    }

    // Select Dropdown Utilities ****
    @Step("Select the value from the dropdown {0} with index {1}")
    public void doSelectDropdownByIndex(By locator, int index) {
        Select select = new Select(getElement(locator));
        select.selectByIndex(index);
    }

    @Step("Select the value from the dropdown {0} with text {1}")
    public void doSelectDropdownByVisibleText(By locator, String text) {
        Select select = new Select(getElement(locator));
        select.selectByVisibleText(text);
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void doSelectDropdownByValue(By locator, String value) {
        Select select = new Select(getElement(locator));
        select.selectByValue(value);
    }

    @Step("get the count of elements on the page using the locator {0}")
    public int getDropdownOptionsCount(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> webElementOptions = select.getOptions();
        System.out.println("Country value size " + webElementOptions.size());
        return webElementOptions.size();
    }

    @Step("get the count of elements on the page using the locator {0}")
    public List<String> getDropdownOptionsTextList(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> optionsList = select.getOptions();
        List<String> optionValueList = new ArrayList<>();
        for (WebElement element : optionsList) {
            String text = element.getText();
            optionValueList.add(text);
        }
        return optionValueList;
    }

    @Step("get the count of elements on the page using the locator {0}")
    public void printDropdownOptionsText(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> webElementOptions = select.getOptions();

        for (WebElement e : webElementOptions) {
            String text = e.getText();
            System.out.println(text);
        }
        System.out.println("***** End of the List");
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void selectValueFromDropDown(By locator, String value) {
        Select select = new Select(getElement(locator));

        List<WebElement> optionsList = select.getOptions();
        boolean flag = false;
        for (WebElement e : optionsList) {
            String text = e.getText();
            System.out.println(text);
            if (text.contains(value)) {
                e.click();
                flag = true;
                break;
            }
        }

        if (flag) {
            System.out.println(value + " is available and selected");
        } else {
            System.out.println(value + " is not available");
        }
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void doSelectDropdownByContainsText(By locator, String value) {
        Select select = new Select(getElement(locator));
        select.selectByContainsVisibleText(value);
    }

    @Step("Select the value from the dropdown {0} with value {1} without using Select class")
    public void selectDropDownValueWithOutSelectClass(By locator, String value) {
        List<WebElement> optionList = getElements(locator);
        boolean flag = false;
        for (WebElement e : optionList) {
            String text = e.getText();
            System.out.println(text);
            if (text.contains(value)) {
                e.click();
                flag = true;
                break;
            }
        }

        if (flag) {
            System.out.println(value + " is available and selected");
        } else {
            System.out.println(value + " is not available");
        }
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void selectMultipleValuesInDropDown(By locator, String value) {
        Select select = new Select(getElement(locator));
        System.out.println(select.isMultiple());
        select.selectByVisibleText(value);
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void deSelectMultipleValuesInDropDown(By locator, String value) {
        Select select = new Select(getElement(locator));
        System.out.println(select.isMultiple());
        select.deselectByVisibleText(value);
    }

    @Step("Select the value from the dropdown {0} with value {1}")
    public void doSearchMethod(By searchField, By suggestions, String searchKey, String actualValue) throws InterruptedException {
        doSendKeys(searchField, searchKey);
        Thread.sleep(2000);

        List<WebElement> suggList = getElements(suggestions);
        System.out.println(suggList.size());

        for (WebElement e : suggList) {
            String text = e.getText();
            System.out.println(text);
            if (text.contains(actualValue)) {
                e.click();
                break;
            }
        }
    }

    /*
     * This method is handling single/multiple and all the choices selection. Please pass 'all' to select all the choices
     * selectChoice(By choiceDropDown, By choices, String... choiceValue)
     * @param choiceDropDown
     * @param choices
     * @param choiceValue
     * @throws InterruptedException
     */
    public void selectChoice(By choiceDropDown, By choices, String... choiceValue) throws InterruptedException {

        doClick(choiceDropDown);
        Thread.sleep(4000L);

        List<WebElement> choicesList = getElements(choices);
        System.out.println(choicesList.size());

        if (choiceValue[0].equalsIgnoreCase("all")) {
            //Select all the choice : one by one
            for (WebElement element : choicesList) {
                element.click();
            }
        } else {
            for (WebElement element : choicesList) {
                String text = element.getText();
                System.out.println(text);
                for (String ch : choiceValue) {
                    if (text.equals(ch)) {
                        element.click();
                    }
                }
            }
        }
    }

    // Actions Class Utils

    public void doActionSendKeys(By locator, CharSequence... value) throws InterruptedException {
        Actions actions = new Actions(driver);
        actions.sendKeys(getElement(locator), value).build().perform();
        Thread.sleep(4000L);
    }

    public void doActionClick(By locator) {
        Actions actions = new Actions(driver);
        actions.click(getElement(locator)).build().perform();

    }

    public void handleTwoLevelMenuSubMenuHandling(By parentMenuLocator, By childMenuLocator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(getElement(parentMenuLocator)).build().perform();
        delay(2000L);
        getElement(childMenuLocator).click();
    }

    public void handleFourLevelMenuSubMenuHandling(By level1Menu, By level2Menu, By level3Menu, By level4Menu) {
        getElement(level1Menu).click();
        delay(3000L);
        Actions actions = new Actions(driver);
        actions.moveToElement(getElement(level2Menu)).build().perform();
        delay(2000L);
        actions.moveToElement(getElement(level3Menu)).build().perform();
        delay(2000L);
        getElement(level4Menu).click();
    }

    public void doSendKeysWithPause(By locator, String value, long pauseDuration) {
        Actions actions = new Actions(driver);
        char[] nameCharArray = value.toCharArray();

        for (char ch : nameCharArray) {
            actions.sendKeys(getElement(locator), String.valueOf(ch))
                    .pause(500).perform();
        }
    }

    //Explicit Waits Utils
    @Step("Wait for WebElement to Present in the Webpage for the locator: {0}")
    public WebElement waitForElementToBePresent(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        highlightElement(element);
        return element;
    }

    @Step("Wait for WebElement to Visible in the Webpage for the locator: {0}")
    public WebElement waitForElementVisible(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        highlightElement(element);
        return element;
    }

    /* An expectation for checking that an element is visible and enabled such that you can click it.
     * We can use it for checkbox, links, radio button, buttons, anything which is clickable we can use it
     * @param locator
     * @param timeout
     */
    @Step("check for WebElement to Ready in the Webpage for the locator: {0}")
    public void clickElementWhenReady(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        highlightElement(element);
        element.click();
    }


    //***** Wait For Title and URL *******
    /*
     * This method is used to wait for the title of the page to be loaded.
     * @param fractionTitle
     * @param timeOut
     * @return
     */
    public String waitForTitleContains(String fractionTitle, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            if (wait.until(ExpectedConditions.titleContains(fractionTitle))) {
                return driver.getTitle();
            }
        } catch (TimeoutException exception) {
            System.out.println("Title is not found after " + timeOut + " seconds");

        }
        return null;
    }

    @Step("Waiting for the title : {0} within the timeout {1}")
    public String waitForTitleIs(String title, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            if (wait.until(ExpectedConditions.titleIs(title))) {
                return driver.getTitle();
            }
        } catch (TimeoutException exception) {
            System.out.println("Title is not found after " + timeOut + " seconds");
        }
        return null;
    }

    public String waitForUrlContains(String fractionUrl, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        try {
            if (wait.until(ExpectedConditions.urlContains(fractionUrl))) {
                return driver.getCurrentUrl();
            }
        } catch (TimeoutException exception) {
            System.out.println("URL is not found after " + timeOut + " seconds");

        }
        return null;
    }

    public String waitForUrlToBe(String url, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        try {
            if (wait.until(ExpectedConditions.urlToBe(url))) {
                return driver.getCurrentUrl();
            }
        } catch (TimeoutException exception) {
            System.out.println("URL is not found after " + timeOut + " seconds");

        }
        return null;
    }

    //***** Wait For Alert Utils *******
    //Alert Utils
    public Alert waitForAlert(long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    public String getAlertText(long timeout) {
        return waitForAlert(timeout).getText();
    }

    public void acceptAlert(long timeout) {
        waitForAlert(timeout).accept();
    }

    public void dismissAlert(long timeout) {
        waitForAlert(timeout).dismiss();
    }

    public void sendKeysToAlert(long timeout, String keys) {
        waitForAlert(timeout).sendKeys(keys);
    }

    //***** Wait For Frame *******
    //Frame Handling using Explicit Wait
    public void waitForFrameByLocatorAndSwitchToFrame(By frameLocator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public void waitForFrameByIndexAndSwitchToFrame(int frameIndex, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
    }

    public void waitForFrameByNameAndSwitchToFrame(String frameIdOrName, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIdOrName));
    }

    public void waitForFrameByElementAndSwitchToFrame(WebElement frameElement, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    //***** Wait For Window *******
    public boolean waitForWindow(int numberOfWindows, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            return wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
        } catch (TimeoutException exception) {
            System.out.println("Window is not found after " + timeout + " seconds");
            return false;
        }
    }

    //***** Wait For Elements *******

    /* An Expectation for checking that there is at least one element present on the web page.
     * Then will give List<WebElement> of elements
     * @param locator
     * @param timeout
     * @return
     */
    public List<WebElement> waitForElementsToBePresent(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException exception) {
            return Collections.emptyList();
        }
    }

    /* An Expectation for checking that all elements are present on the web page that match the locator are visible.
     * Visibility means that elements are not only displayed but also have a height and width that is greater than 0.
     * Then will give List<WebElement> of elements
     * @param locator
     * @param timeout
     * @return
     */
    public List<WebElement> waitForElementsToVisible(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException exception) {
            return Collections.emptyList();
        }
    }

    /* A Method will check for the page is completely loaded or not.
     *
     * @param timeout
     * @return
     */
    public boolean waitForPageLoading(long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        String isPageLoaded = wait.until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';")).toString();
        return Boolean.parseBoolean(isPageLoaded);
    }

    //Polling Time
    public WebElement waitForElementVisible(By locator, long timeout, long pollingtime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(pollingtime));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBePresent(By locator, long timeout, long pollingtime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(pollingtime));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    //Fluent Wait
    public WebElement waitForElementVisibleUsingFluentFeatures(By locator, long timeout, long pollingTime) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Element not found within the given time frame");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Wait for Alert using Fluent Features
    public Alert waitForAlertUsingFluentFeatures(long timeout, long pollingTime) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoAlertPresentException.class)
                .withMessage("Alert not found within the given time frame");
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    //Wait for Frame using Fluent Features
    public void waitForFrameByLocatorAndSwitchToFrameUsingFluentFeatures(By frameLocator, long timeout, long pollingTime) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoSuchFrameException.class)
                .withMessage("Frame is not found within the given time frame");
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }


}
