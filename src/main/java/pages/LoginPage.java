package pages;

import com.aventstack.chaintest.plugins.ChainTestListener;
import constants.AppConstants;
import factory.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import utils.ElementUtil;


public class LoginPage {

    WebDriver driver;
    private ElementUtil elementUtil;
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    //1. By Locators : Page Objects -> OR

    private final By email = By.id("input-email");
    private final By password = By.id("input-password");
    private final By loginBtn = By.xpath("//input[@value='Login']");
    private final By forgotPwd = By.linkText("Forgotten Password");
    private final By registerLink = By.linkText("Register");


    //2. Page Methods or Actions --> Feature of the Page
    @Step("get Login Title")
    public String getLoginPageTitle() {
        String title = elementUtil.waitForTitleIs(AppConstants.LOGIN_PAGE_TITLE, AppConstants.DEFAULT_TIME_OUT);
        System.out.println("Login Page Title is " + title);
        log.info("Login Page Title is {}", title);
        ChainTestListener.log("Login Page Title is " + title);
        return title;
    }

    @Step("get Login URL")
    public String getLoginPageUrl() {
        String currentUrl = elementUtil.waitForUrlContains(AppConstants.LOGIN_PAGE_URL_FRACTION, AppConstants.DEFAULT_TIME_OUT);
        log.info("Login Page Url is {}", currentUrl);
        return currentUrl;
    }

    @Step("Forgot Password Link ")
    public boolean isForgotPassLinkExist() {
        return elementUtil.doElementIsDisplayed(forgotPwd);
    }

    @Step("login with username : {0} and password : {1}")
    public HomePage doLogin(String userName, String pwd) {
        System.out.println("App credentials are " + userName + " : " + pwd);
        log.info("App credentials are {} : {}", userName, pwd);
        elementUtil.waitForElementVisible(email, AppConstants.SHORT_TIME_OUT).sendKeys(userName);
        elementUtil.doSendKeys(password, pwd);
        elementUtil.doClick(loginBtn);
        return new HomePage(driver);
    }

    @Step("Click on Register")
    public RegistrationPage clickOnRegister(){
        elementUtil.waitForElementVisible(registerLink, AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(registerLink);
        return new RegistrationPage(driver);
    }
}
