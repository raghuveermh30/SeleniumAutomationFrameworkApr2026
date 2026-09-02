package test;

import base.BaseTest;

import com.aventstack.chaintest.plugins.ChainTestListener;
import constants.AppConstants;
import constants.AppError;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test suite covering OpenCart login page functionality: title/URL checks,
 * forgotten-password link, logo, footer links and valid/invalid login flows.
 */
@Epic("SUP-12345 - Login Page Design for OpenCart Application")
@Story("SUP-100 - Login Page Features for OpenCart Application")
@Feature("SUP-1 - Login Page Test Features")
public class LoginPageTest extends BaseTest {

    /**
     * Verifies the login page title matches the expected value.
     */
    @Description("Login Page Title Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(description = "Login Page Title Test")
    public void loginPageTitleTest() {
        ChainTestListener.log("Verifying Login Page Title");
        String title = loginPage.getLoginPageTitle();
        Assert.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE, AppError.LOGIN_PAGE_TITLE_ERROR);
    }

    /**
     * Verifies the login page URL contains the expected fragment.
     */
    @Description("Login Page URL Test")
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test(description = "Login Page URL Test")
    public void loginPageUrlTest() {
        ChainTestListener.log(">>loginPageUrlTest()");
        String loginPageUrl = loginPage.getLoginPageUrl();
        Assert.assertTrue(loginPageUrl.contains(AppConstants.LOGIN_PAGE_URL_FRACTION), AppError.LOGIN_PAGE_URL_ERROR);
    }

    /**
     * Verifies the Forgotten Password link is present on the login page.
     */
    @Description("Forgot Password Link Test")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test(description = "Forgot Password Link Exist Test")
    public void forgotPwdLinkExistTest() {
        ChainTestListener.log(">>forgotPwdLinkExistTest()");
        Assert.assertTrue(loginPage.isForgotPassLinkExist(), AppError.FORGOT_PWD_LINK_ERROR);
    }

    /**
     * Verifies the site logo is displayed on the login page.
     */
    @Description("Logo Displayed Test on Home Page")
    @Test(description = "Logo Displayed Test", priority = 2)
    public void isLogoDisplayed() {
        ChainTestListener.log(">>isLogoDisplayed()");
        Assert.assertTrue(commonPage.isLogoDisplayed(), AppError.LOGO_NOT_DISPLAYED_ERROR);
    }

    /**
     * Verifies each expected footer link is present on the login page.
     *
     * @param linkName the footer link text to check for
     */
    @Test(dataProvider = "footerLinksData", dataProviderClass = utils.TestDataUtil.class,
            description = "Footer Links Test", enabled = true, priority = 3)
    public void getFooterLink(String linkName) {
        ChainTestListener.log(">>getFooterLink()");
        List<String> footerLinks = commonPage.getFooterLinks();
        footerLinks.forEach(System.out::println);
        Assert.assertTrue(commonPage.checkFooterLink(linkName));
    }

    /**
     * Verifies a successful login with valid credentials lands on the home page.
     */
    @Description("Login Test with valid credentials")
    @Owner("Naveen Automation Labs")
    @Test(priority = 1, description = "Login Test")
    public void loginTest() {
        ChainTestListener.log(">>loginTest()");
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
        Assert.assertEquals(homePage.getHomePageTitle(), "My Account", "=== Home Page Title is not Matched ===");
    }

    /**
     * Verifies login fails and shows a warning when the password is incorrect.
     */
    @Description("Invalid Login Test - Wrong Password")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test(priority = -1, description = "Login should fail and show warning for wrong password")
    public void invalidLoginWithWrongPasswordTest() {
        ChainTestListener.log(">>invalidLoginWithWrongPasswordTest()");
        loginPage.doLogin(properties.getProperty("username"), "WrongPassword@999");
        String warningMsg = loginPage.getLoginWarningMessage();
        Assert.assertTrue(warningMsg.contains("Warning:"), AppError.INVALID_LOGIN_ERROR);
    }

    /**
     * Verifies login fails and shows a warning when credentials are empty.
     */
    @Description("Invalid Login Test - Empty Credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test(priority = -2, description = "Login should fail and show warning for empty credentials")
    public void loginWithEmptyCredentialsTest() {
        ChainTestListener.log(">>loginWithEmptyCredentialsTest()");
        loginPage.doLogin("", "");
        String warningMsg = loginPage.getLoginWarningMessage();
        Assert.assertTrue(warningMsg.contains("Warning:"), AppError.INVALID_LOGIN_ERROR);
    }


}
