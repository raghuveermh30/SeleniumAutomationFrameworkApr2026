package test;

import base.BaseTest;
import com.aventstack.chaintest.plugins.ChainTestListener;
import constants.AppError;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test suite covering the OpenCart logout flow from the home page.
 */
@Epic("SUP-12348 - Logout Page Design for OpenCart Application")
@Story("SUP-103 - Logout Page Features for OpenCart Application")
@Feature("SUP-4 - Logout Page Test Features")
public class LogoutPageTest extends BaseTest {

    /**
     * Logs in before the logout test runs, populating {@code homePage}.
     */
    @BeforeClass
    public void doLogin() {
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
    }

    /**
     * Verifies logging out from the home page shows the logout confirmation message.
     */
    @Description("Logout from Home Page and verify logout confirmation message")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test(description = "Logout from Home Page Test")
    public void logoutFromHomePageTest() {
        ChainTestListener.log(">>logoutFromHomePageTest()");
        logoutPage = homePage.doLogout();
        Assert.assertTrue(logoutPage.isLogoutMsgDisplayed(), AppError.LOGOUT_MSG_NOT_DISPLAYED_ERROR);
    }
}
