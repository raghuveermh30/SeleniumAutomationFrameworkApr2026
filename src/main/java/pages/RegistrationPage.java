package pages;

import constants.AppConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;

public class RegistrationPage {

    WebDriver driver;
    ElementUtil elementUtil;

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

    public String doRegister(String fName, String lName, String emailId, String telPhn, String pwd, String confirmpassword) {
        elementUtil.waitForElementVisible(firstName, AppConstants.DEFAULT_TIME_OUT);
        elementUtil.doSendKeys(firstName, fName);
        elementUtil.doSendKeys(lastName, lName);
        emailId = String.valueOf(System.currentTimeMillis()).substring(7) + emailId;
        elementUtil.doSendKeys(email, emailId);
        elementUtil.doSendKeys(telephone, telPhn);
        elementUtil.doSendKeys(password, pwd);
        elementUtil.doSendKeys(confirmPassword, confirmpassword);
        elementUtil.doClick(checkBox);
        elementUtil.doClick(continueBtn);
        return elementUtil.doElementGetText(successMsg);
    }

    @Step("check if registration success message exists")
    public boolean isRegistrationSuccessMessageExist() {
        elementUtil.waitForElementVisible(successMsg, AppConstants.MEDIUM_TIME_OUT);
        return elementUtil.doElementIsDisplayed(successMsg);
    }

    @Step("click on logout link")
    public LogoutPage clickOnLogoutLink() {
        elementUtil.waitForElementVisible(logoutLink, AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(logoutLink);
        return new LogoutPage(driver);
    }

}

