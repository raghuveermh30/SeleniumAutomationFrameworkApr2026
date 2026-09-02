package pages;

import constants.AppConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;
import utils.StringUtil;

/**
 * Page object representing the OpenCart new-account registration page.
 * <p>
 * Exposes actions for submitting the registration form, validating
 * success/error states, and logging out after registration.
 */
public class RegistrationPage {

    WebDriver driver;
    ElementUtil elementUtil;

    /**
     * Creates a new RegistrationPage bound to the given driver.
     *
     * @param driver the WebDriver instance controlling the browser
     */
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    // Locators
    private final By firstName = By.id("input-firstname");
    private final By lastName = By.id("input-lastname");
    private final By email = By.id("input-email");
    private final By telephone = By.id("input-telephone");
    private final By password = By.id("input-password");
    private final By confirmPassword = By.id("input-confirm");
    private final By checkBox = By.name("agree");
    private final By continueBtn = By.xpath("//input[@value='Continue']");
    private final By successMsg = By.cssSelector("div#content h1");
    private final By logoutLink = By.linkText("Logout");
    private final By validationError = By.cssSelector(".text-danger");

    /**
     * Fills in and submits the registration form. The supplied email is made
     * unique via {@link StringUtil#getUniqueEmail(String)} before submission.
     *
     * @param fName first name
     * @param lName last name
     * @param emailId base email address (will be made unique)
     * @param telPhn telephone number
     * @param pwd account password
     * @param confirmpassword password confirmation
     * @return the success message text shown after registration
     */
    @Step("register user with email : {2}")
    public String doRegister(String fName, String lName, String emailId, String telPhn, String pwd, String confirmpassword) {
        elementUtil.waitForElementVisible(firstName, AppConstants.DEFAULT_TIME_OUT);
        elementUtil.doSendKeys(firstName, fName);
        elementUtil.doSendKeys(lastName, lName);
        String uniqueEmail = StringUtil.getUniqueEmail(emailId);
        elementUtil.doSendKeys(email, uniqueEmail);
        elementUtil.doSendKeys(telephone, telPhn);
        elementUtil.doSendKeys(password, pwd);
        elementUtil.doSendKeys(confirmPassword, confirmpassword);
        elementUtil.doClick(checkBox);
        elementUtil.doClick(continueBtn);
        return elementUtil.doElementGetText(successMsg);
    }

    /**
     * Checks whether the registration success message is displayed.
     *
     * @return true if the success message is displayed, false otherwise
     */
    @Step("check if registration success message exists")
    public boolean isRegistrationSuccessMessageExist() {
        elementUtil.waitForElementVisible(successMsg, AppConstants.MEDIUM_TIME_OUT);
        return elementUtil.doElementIsDisplayed(successMsg);
    }

    /**
     * Clicks the Logout link and returns the resulting logout page.
     *
     * @return a new {@link LogoutPage}
     */
    @Step("click on logout link")
    public LogoutPage clickOnLogoutLink() {
        elementUtil.waitForElementVisible(logoutLink, AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(logoutLink);
        return new LogoutPage(driver);
    }

    /**
     * Submits the registration form without filling in any fields, to
     * trigger validation errors.
     */
    @Step("submit registration form without filling any fields")
    public void submitWithEmptyFields() {
        elementUtil.waitForElementVisible(continueBtn, AppConstants.DEFAULT_TIME_OUT);
        elementUtil.doClick(continueBtn);
    }

    /**
     * Checks whether any field validation errors are displayed on the page.
     *
     * @return true if at least one validation error is displayed, false otherwise
     */
    @Step("check if validation errors are displayed")
    public boolean hasValidationErrors() {
        return elementUtil.getElements(validationError).size() > 0;
    }

}

