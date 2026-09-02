package utils;


import constants.AppConstants;
import factory.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Canonical wrapper around Selenium's low-level WebDriver/WebElement APIs.
 * <p>
 * Provides element lookup, interaction (click/type), dropdown, alert, frame,
 * window and explicit/fluent wait utilities used by all page objects.
 * Prefer these methods over calling {@code driver.findElement}/{@code WebElement} directly.
 */
public class ElementUtil {

    private final WebDriver driver;
    private JavaScriptExecutorUtil javaScriptExecutorUtil;

    /**
     * Creates a new ElementUtil bound to the given driver.
     *
     * @param driver the WebDriver instance to operate on
     */
    public ElementUtil(WebDriver driver) {
        this.driver = driver;
        javaScriptExecutorUtil = new JavaScriptExecutorUtil(driver);
    }

    private void highlightElement(WebElement element) {
        if (Boolean.parseBoolean(DriverFactory.highlight)) {
            javaScriptExecutorUtil.flash(element);
        }
    }

    /**
     * Finds and returns the element for the given locator, highlighting it
     * when the {@code highlight} property is enabled.
     *
     * @param locator the By locator identifying the element
     * @return the located WebElement
     */
    public WebElement getElement(By locator) {
        //waitForElementVisible(locator, 10); //default wait is 10 seconds
        //waitForElementVisible -> this method will be auto wait feature and it will wait for all the elements unnecessarily.
        //The above method will slow down the performance of the script.

        WebElement element = driver.findElement(locator);
        highlightElement(element);
        return element;
    }

    /**
     * Clears the element identified by the locator and types the given value into it.
     *
     * @param locator the By locator identifying the element
     * @param value the characters to type
     */
    @Step("Fill the values for Webelement: {0} and the value is: {1}")
    public void doSendKeys(By locator, CharSequence... value) {
        nullCheck(value);
        WebElement element = getElement(locator);
        waitForElementsToBePresent(locator, AppConstants.MEDIUM_TIME_OUT);
        element.clear();
        delay(1000);
        element.sendKeys(value);
    }

    /**
     * Clears the element identified by the given locator type/value and types the given value into it.
     *
     * @param locatorType the type of locator (e.g. "ID", "XPATH", "CSS")
     * @param locatorValue the locator value
     * @param value the characters to type
     */
    @Step("Fill the values for Webelement: {0} and the value is: {1}")
    public void doSendKeys(String locatorType, String locatorValue, CharSequence... value) {
        nullCheck(value);
        WebElement element = getElement(getLocator(locatorType, locatorValue));
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Clears the given element and types the given value into it.
     *
     * @param element the WebElement to interact with
     * @param value the characters to type
     */
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

    /**
     * Clicks the element identified by the given locator.
     *
     * @param locator the By locator identifying the element
     */
    @Step("Click the Webelement")
    public void doClick(By locator) {
        getElement(locator).click();
    }

    /**
     * Clicks the element identified by the given locator type/value.
     *
     * @param locatorType the type of locator (e.g. "ID", "XPATH", "CSS")
     * @param locatorValue the locator value
     */
    @Step("Clicking on the WebElement {0}")
    public void doClick(String locatorType, String locatorValue) {
        getElement(getLocator(locatorType, locatorValue)).click();
    }

    /**
     * Waits for the element to be visible and returns its visible text.
     *
     * @param locator the By locator identifying the element
     * @return the element's text
     */
    @Step("Get the Element Text on the WebElement: {0}")
    public String doElementGetText(By locator) {
        // waitForElementVisible(locator, AppConstants.DEFAULT_TIME_OUT);
        waitForElementsToVisible(locator, AppConstants.DEFAULT_TIME_OUT);
        return getElement(locator).getText();
    }

    /**
     * Returns the text of the element identified by the given locator type/value.
     *
     * @param locatorType the type of locator (e.g. "ID", "XPATH", "CSS")
     * @param locatorValue the locator value
     * @return the element's text
     */
    public String getElementText(String locatorType, String locatorValue) {
        return getElement(getLocator(locatorType, locatorValue)).getText();
    }

    /**
     * Builds a {@link By} locator from a locator type/value pair.
     *
     * @param locatorType the type of locator (ID, NAME, CLASS NAME, XPATH, CSS, LINK TEXT, PARTIAL LINK TEXT, TAG NAME)
     * @param locatorValue the locator value
     * @return the constructed By locator
     * @throws IllegalArgumentException if the locator type is not supported
     */
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


    /**
     * Returns all elements matching the given locator.
     *
     * @param locator the By locator identifying the elements
     * @return a list of matching WebElements (may be empty)
     */
    @Step("get list of WebElements using the locator {0}")
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Checks whether exactly one element matches the given locator.
     *
     * @param locator the By locator identifying the element
     * @return true if exactly one matching element exists, false otherwise
     */
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

    /**
     * Checks whether the number of elements matching the given locator equals the expected count.
     *
     * @param locator the By locator identifying the elements
     * @param eleCount the expected number of matching elements
     * @return true if the actual count equals {@code eleCount}, false otherwise
     */
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

    /**
     * Finds and returns the element for the given locator without highlighting.
     *
     * @param locator the By locator identifying the element
     * @return the located WebElement
     */
    @Step("get Webelement using the locator {0}")
    public WebElement getFindElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Checks whether the element identified by the locator is displayed,
     * returning false instead of throwing if it does not exist.
     *
     * @param locator the By locator identifying the element
     * @return true if the element exists and is displayed, false otherwise
     */
    @Step("element {0} is displayed..")
    public boolean doElementIsDisplayed(By locator) {
        try {
            return getFindElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Element is not displayed");
            return false;
        }
    }

    /**
     * Returns the value of the given DOM attribute for the located element.
     *
     * @param locator the By locator identifying the element
     * @param attributeName the DOM attribute name
     * @return the attribute value
     */
    @Step("get the count of elements on the page using the locator {0}")
    public String doGetDomAttribute(By locator, String attributeName) {
        nullCheck(attributeName);
        return getElement(locator).getDomAttribute(attributeName);
    }

    /**
     * Returns the value of the given DOM property for the located element.
     *
     * @param locator the By locator identifying the element
     * @param propName the DOM property name
     * @return the property value
     */
    @Step("get the count of elements on the page using the locator {0}")
    public String doGetDomProperty(By locator, String propName) {
        nullCheck(propName);
        return getElement(locator).getDomProperty(propName);
    }

    /**
     * Returns the non-empty text of all elements matching the given locator.
     *
     * @param locator the By locator identifying the elements
     * @return a list of non-empty element texts
     */
    @Step("get the count of elements on the page using the locator {0}")
    public List<String> fetchElementsText(By locator) {
        List<String> eleTextList = new ArrayList<>();
        getElements(locator).stream().filter(e -> !e.getText().isEmpty()).forEach(e -> eleTextList.add(e.getText()));
        return eleTextList;
    }

    /**
     * Prints the non-empty text of all elements matching the given locator to standard out.
     *
     * @param locator the By locator identifying the elements
     */
    @Step("get the count of elements on the page using the locator {0}")
    public void getElementsText(By locator) {
        getElements(locator).stream().filter(e -> !e.getText().isEmpty()).forEach(e -> System.out.println(e.getText()));
    }

    // Select Dropdown Utilities ****
    /**
     * Selects a dropdown option by its zero-based index.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @param index the zero-based index of the option to select
     */
    @Step("Select the value from the dropdown {0} with index {1}")
    public void doSelectDropdownByIndex(By locator, int index) {
        Select select = new Select(getElement(locator));
        select.selectByIndex(index);
    }

    /**
     * Selects a dropdown option by its exact visible text.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @param text the visible text of the option to select
     */
    @Step("Select the value from the dropdown {0} with text {1}")
    public void doSelectDropdownByVisibleText(By locator, String text) {
        Select select = new Select(getElement(locator));
        select.selectByVisibleText(text);
    }

    /**
     * Selects a dropdown option by its {@code value} attribute.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @param value the value attribute of the option to select
     */
    @Step("Select the value from the dropdown {0} with value {1}")
    public void doSelectDropdownByValue(By locator, String value) {
        Select select = new Select(getElement(locator));
        select.selectByValue(value);
    }

    /**
     * Returns the number of options available in the dropdown.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @return the count of dropdown options
     */
    @Step("get the count of elements on the page using the locator {0}")
    public int getDropdownOptionsCount(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> webElementOptions = select.getOptions();
        System.out.println("Country value size " + webElementOptions.size());
        return webElementOptions.size();
    }

    /**
     * Returns the visible text of all options in the dropdown.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @return a list of option texts
     */
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

    /**
     * Prints the visible text of all options in the dropdown to standard out.
     *
     * @param locator the By locator identifying the {@code <select>} element
     */
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

    /**
     * Selects the dropdown option whose text contains the given value.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @param value the substring to match against option text
     */
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

    /**
     * Selects the dropdown option whose visible text contains the given value.
     *
     * @param locator the By locator identifying the {@code <select>} element
     * @param value the substring to match against option text
     */
    @Step("Select the value from the dropdown {0} with value {1}")
    public void doSelectDropdownByContainsText(By locator, String value) {
        Select select = new Select(getElement(locator));
        select.selectByContainsVisibleText(value);
    }

    /**
     * Selects a dropdown option by clicking the matching element, without using the {@link Select} class.
     *
     * @param locator the By locator identifying the dropdown option elements
     * @param value the substring to match against option text
     */
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

    /**
     * Selects a value in a multi-select dropdown by its visible text.
     *
     * @param locator the By locator identifying the multi-select element
     * @param value the visible text of the option to select
     */
    @Step("Select the value from the dropdown {0} with value {1}")
    public void selectMultipleValuesInDropDown(By locator, String value) {
        Select select = new Select(getElement(locator));
        System.out.println(select.isMultiple());
        select.selectByVisibleText(value);
    }

    /**
     * Deselects a value in a multi-select dropdown by its visible text.
     *
     * @param locator the By locator identifying the multi-select element
     * @param value the visible text of the option to deselect
     */
    @Step("Select the value from the dropdown {0} with value {1}")
    public void deSelectMultipleValuesInDropDown(By locator, String value) {
        Select select = new Select(getElement(locator));
        System.out.println(select.isMultiple());
        select.deselectByVisibleText(value);
    }

    /**
     * Types the search key into the search field, waits briefly for suggestions
     * to load, then clicks the suggestion matching the actual value.
     *
     * @param searchField the By locator for the search input
     * @param suggestions the By locator for the suggestion list items
     * @param searchKey the text to type into the search field
     * @param actualValue the suggestion text to match and click
     * @throws InterruptedException if the wait between typing and reading suggestions is interrupted
     */
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

    /**
     * Handles single/multiple/all choice selection from a dropdown-driven choice list.
     * Pass {@code "all"} as the only choice value to select every available choice.
     *
     * @param choiceDropDown the By locator for the element that opens the choice list
     * @param choices the By locator for the individual choice elements
     * @param choiceValue one or more choice texts to select, or {@code "all"} to select every choice
     * @throws InterruptedException if the wait for the choice list to render is interrupted
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

    /**
     * Sends keys to the located element using the {@link Actions} class.
     *
     * @param locator the By locator identifying the element
     * @param value the characters to send
     * @throws InterruptedException if the post-action wait is interrupted
     */
    public void doActionSendKeys(By locator, CharSequence... value) throws InterruptedException {
        Actions actions = new Actions(driver);
        actions.sendKeys(getElement(locator), value).build().perform();
        Thread.sleep(4000L);
    }

    /**
     * Clicks the located element using the {@link Actions} class.
     *
     * @param locator the By locator identifying the element
     */
    public void doActionClick(By locator) {
        Actions actions = new Actions(driver);
        actions.click(getElement(locator)).build().perform();

    }

    /**
     * Hovers over a parent menu item and clicks the resulting child menu item.
     *
     * @param parentMenuLocator the By locator for the parent menu item
     * @param childMenuLocator the By locator for the child menu item
     */
    public void handleTwoLevelMenuSubMenuHandling(By parentMenuLocator, By childMenuLocator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(getElement(parentMenuLocator)).build().perform();
        delay(2000L);
        getElement(childMenuLocator).click();
    }

    /**
     * Navigates a four-level nested menu by clicking the top level and
     * hovering through the intermediate levels before clicking the final item.
     *
     * @param level1Menu the By locator for the first (top) menu level
     * @param level2Menu the By locator for the second menu level
     * @param level3Menu the By locator for the third menu level
     * @param level4Menu the By locator for the fourth (final) menu level
     */
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

    /**
     * Types the given value into the located element one character at a time,
     * pausing between each keystroke.
     *
     * @param locator the By locator identifying the element
     * @param value the text to type
     * @param pauseDuration reserved for the pause duration (currently a fixed 500ms pause is used per character)
     */
    public void doSendKeysWithPause(By locator, String value, long pauseDuration) {
        Actions actions = new Actions(driver);
        char[] nameCharArray = value.toCharArray();

        for (char ch : nameCharArray) {
            actions.sendKeys(getElement(locator), String.valueOf(ch))
                    .pause(500).perform();
        }
    }

    //Explicit Waits Utils
    /**
     * Waits until the element is present in the DOM, then returns it.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     * @return the located WebElement
     */
    @Step("Wait for WebElement to Present in the Webpage for the locator: {0}")
    public WebElement waitForElementToBePresent(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        highlightElement(element);
        return element;
    }

    /**
     * Waits until the element is visible, then returns it.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     * @return the visible WebElement
     */
    @Step("Wait for WebElement to Visible in the Webpage for the locator: {0}")
    public WebElement waitForElementVisible(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        highlightElement(element);
        return element;
    }

    /**
     * Waits until the element is visible and clickable, then clicks it.
     * Suitable for checkboxes, links, radio buttons and any clickable element.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     */
    @Step("check for WebElement to Ready in the Webpage for the locator: {0}")
    public void clickElementWhenReady(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        highlightElement(element);
        element.click();
    }


    //***** Wait For Title and URL *******
    /**
     * Waits until the page title contains the given fraction, then returns the full title.
     *
     * @param fractionTitle the substring expected in the page title
     * @param timeOut max time to wait, in seconds
     * @return the page title, or {@code null} if the timeout elapses
     */
    public String waitForTitleContains(String fractionTitle, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        try {
            if (wait.until(ExpectedConditions.titleContains(fractionTitle))) {
                return driver.getTitle();
            }
        } catch (TimeoutException exception) {
            System.out.println("Title is not found after " + timeOut + " seconds");

        }
        return null;
    }

    /**
     * Waits until the page title exactly equals the given value, then returns it.
     *
     * @param title the expected exact page title
     * @param timeOut max time to wait, in seconds
     * @return the page title, or {@code null} if the timeout elapses
     */
    @Step("Waiting for the title : {0} within the timeout {1}")
    public String waitForTitleIs(String title, long timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        try {
            if (wait.until(ExpectedConditions.titleIs(title))) {
                return driver.getTitle();
            }
        } catch (TimeoutException exception) {
            System.out.println("Title is not found after " + timeOut + " seconds");
        }
        return null;
    }

    /**
     * Waits until the current URL contains the given fraction, then returns the full URL.
     *
     * @param fractionUrl the substring expected in the URL
     * @param timeOut max time to wait, in seconds
     * @return the current URL, or {@code null} if the timeout elapses
     */
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

    /**
     * Waits until the current URL exactly equals the given value, then returns it.
     *
     * @param url the expected exact URL
     * @param timeOut max time to wait, in seconds
     * @return the current URL, or {@code null} if the timeout elapses
     */
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
    /**
     * Waits for a JavaScript alert to be present and returns it.
     *
     * @param timeout max time to wait, in seconds
     * @return the present {@link Alert}
     */
    public Alert waitForAlert(long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits for an alert and returns its text.
     *
     * @param timeout max time to wait, in seconds
     * @return the alert text
     */
    public String getAlertText(long timeout) {
        return waitForAlert(timeout).getText();
    }

    /**
     * Waits for an alert and accepts (clicks OK on) it.
     *
     * @param timeout max time to wait, in seconds
     */
    public void acceptAlert(long timeout) {
        waitForAlert(timeout).accept();
    }

    /**
     * Waits for an alert and dismisses (clicks Cancel on) it.
     *
     * @param timeout max time to wait, in seconds
     */
    public void dismissAlert(long timeout) {
        waitForAlert(timeout).dismiss();
    }

    /**
     * Waits for an alert and sends the given keys to its input field.
     *
     * @param timeout max time to wait, in seconds
     * @param keys the text to send to the alert
     */
    public void sendKeysToAlert(long timeout, String keys) {
        waitForAlert(timeout).sendKeys(keys);
    }

    //***** Wait For Frame *******
    //Frame Handling using Explicit Wait
    /**
     * Waits for a frame identified by locator to be available and switches into it.
     *
     * @param frameLocator the By locator identifying the frame
     * @param timeout max time to wait, in seconds
     */
    public void waitForFrameByLocatorAndSwitchToFrame(By frameLocator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    /**
     * Waits for a frame identified by index to be available and switches into it.
     *
     * @param frameIndex the zero-based index of the frame
     * @param timeout max time to wait, in seconds
     */
    public void waitForFrameByIndexAndSwitchToFrame(int frameIndex, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
    }

    /**
     * Waits for a frame identified by id or name to be available and switches into it.
     *
     * @param frameIdOrName the id or name attribute of the frame
     * @param timeout max time to wait, in seconds
     */
    public void waitForFrameByNameAndSwitchToFrame(String frameIdOrName, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIdOrName));
    }

    /**
     * Waits for the given frame WebElement to be available and switches into it.
     *
     * @param frameElement the frame WebElement to switch to
     * @param timeout max time to wait, in seconds
     */
    public void waitForFrameByElementAndSwitchToFrame(WebElement frameElement, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    //***** Wait For Window *******
    /**
     * Waits until the number of open browser windows equals the expected count.
     *
     * @param numberOfWindows the expected number of windows
     * @param timeout max time to wait, in seconds
     * @return true if the expected window count is reached, false if the timeout elapses
     */
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

    /**
     * Waits until at least one element matching the locator is present in the DOM.
     *
     * @param locator the By locator identifying the elements
     * @param timeout max time to wait, in seconds
     * @return the list of present elements, or an empty list if the timeout elapses
     */
    public List<WebElement> waitForElementsToBePresent(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException exception) {
            return Collections.emptyList();
        }
    }

    /**
     * Waits until all elements matching the locator are visible.
     * Visibility means the elements are displayed and have a height/width greater than 0.
     *
     * @param locator the By locator identifying the elements
     * @param timeout max time to wait, in seconds
     * @return the list of visible elements, or an empty list if the timeout elapses
     */
    public List<WebElement> waitForElementsToVisible(By locator, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException exception) {
            return Collections.emptyList();
        }
    }

    /**
     * Waits until the page's {@code document.readyState} equals "complete".
     *
     * @param timeout max time to wait, in seconds
     * @return true if the page finished loading before the timeout, false otherwise
     */
    public boolean waitForPageLoading(long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        String isPageLoaded = wait.until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';")).toString();
        return Boolean.parseBoolean(isPageLoaded);
    }

    //Polling Time
    /**
     * Waits until the element is visible, polling at the given interval.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     * @param pollingtime the polling interval, in seconds
     * @return the visible WebElement
     */
    public WebElement waitForElementVisible(By locator, long timeout, long pollingtime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(pollingtime));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is present in the DOM, polling at the given interval.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     * @param pollingtime the polling interval, in seconds
     * @return the located WebElement
     */
    public WebElement waitForElementToBePresent(By locator, long timeout, long pollingtime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(pollingtime));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    //Fluent Wait
    /**
     * Waits until the element is visible using a {@link FluentWait}, ignoring
     * {@link NoSuchElementException} and {@link StaleElementReferenceException} during polling.
     *
     * @param locator the By locator identifying the element
     * @param timeout max time to wait, in seconds
     * @param pollingTime the polling interval, in seconds
     * @return the visible WebElement
     */
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
    /**
     * Waits for a JavaScript alert to be present using a {@link FluentWait}.
     *
     * @param timeout max time to wait, in seconds
     * @param pollingTime the polling interval, in seconds
     * @return the present {@link Alert}
     */
    public Alert waitForAlertUsingFluentFeatures(long timeout, long pollingTime) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoAlertPresentException.class)
                .withMessage("Alert not found within the given time frame");
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    //Wait for Frame using Fluent Features
    /**
     * Waits for a frame identified by locator to become available using a
     * {@link FluentWait}, then switches into it.
     *
     * @param frameLocator the By locator identifying the frame
     * @param timeout max time to wait, in seconds
     * @param pollingTime the polling interval, in seconds
     */
    public void waitForFrameByLocatorAndSwitchToFrameUsingFluentFeatures(By frameLocator, long timeout, long pollingTime) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoSuchFrameException.class)
                .withMessage("Frame is not found within the given time frame");
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }


}
