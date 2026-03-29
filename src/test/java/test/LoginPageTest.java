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

@Epic("SUP-12345 - Login Page Design for OpenCart Application")
@Story("SUP-100 - Login Page Features for OpenCart Application")
@Feature("SUP-1 - Login Page Test Features")
public class LoginPageTest extends BaseTest {

    @Description("Login Page Title Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(description = "Login Page Title Test")
    public void loginPageTitleTest() {
        ChainTestListener.log("Verifying Login Page Title");
        String title = loginPage.getLoginPageTitle();
        Assert.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE, AppError.LOGIN_PAGE_TITLE_ERROR);
    }

    @Description("Login Page URL Test")
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test(description = "Login Page URL Test")
    public void loginPageUrlTest() {
        ChainTestListener.log(">>loginPageUrlTest()");
        String loginPageUrl = loginPage.getLoginPageUrl();
        Assert.assertTrue(loginPageUrl.contains(AppConstants.LOGIN_PAGE_URL_FRACTION), AppError.LOGIN_PAGE_URL_ERROR);
    }

    @Description("Forgot Password Link Test")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test(description = "Forgot Password Link Exist Test")
    public void forgotPwdLinkExistTest() {
        ChainTestListener.log(">>forgotPwdLinkExistTest()");
        Assert.assertTrue(loginPage.isForgotPassLinkExist(), AppError.FORGOT_PWD_LINK_ERROR);
    }

    @Description("Logo Displayed Test on Home Page")
    @Test(description = "Logo Displayed Test", priority = 2)
    public void isLogoDisplayed() {
        ChainTestListener.log(">>isLogoDisplayed()");
        Assert.assertTrue(commonPage.isLogoDisplayed(), AppError.LOGO_NOT_DISPLAYED_ERROR);
    }

    @Test(dataProvider = "getFooterData", description = "Footer Links Test", enabled = true, priority = 3)
    public void getFooterLink(String linkName) {
        ChainTestListener.log(">>getFooterLink()");
        List<String> footerLinks = commonPage.getFooterLinks();
        footerLinks.forEach(System.out::println);
        Assert.assertTrue(commonPage.checkFooterLink(linkName));
    }

    @DataProvider
    public Object[][] getFooterData() {
        return new Object[][]{
                {"Contact Us"},
                {"Delivery Information"},
                {"Returns"},
                {"Brands"},
                {"Gift Certificates"},
                {"Affiliate"},
                {"Specials"},
                {"My Account"}
        };
    }

    @Description("Login Test with valid credentials")
    @Owner("Naveen Automation Labs")
    @Test(priority = 1, description = "Login Test")
    public void loginTest() {
        ChainTestListener.log(">>loginTest()");
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
        Assert.assertEquals(homePage.getHomePageTitle(), "My Account", "=== Home Page Title is not Matched ===");

    }


}
