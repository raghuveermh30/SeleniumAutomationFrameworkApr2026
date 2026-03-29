package test;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelUtil;

public class RegistrationPageTest extends BaseTest {

    @DataProvider
    public Object[][] getRegistrationData() {
        return ExcelUtil.getTestData(AppConstants.REGISTRATION_SHEET_NAME);
    }

    @Test (dataProvider = "getRegistrationData")
    public void userRegistrationTest(String firstName, String lastName, String email, String telephone, String password, String confirmpassword) {
        registrationPage = loginPage.clickOnRegister();
        registrationPage.doRegister(firstName, lastName, email, telephone, password, confirmpassword);
        boolean flag =registrationPage.isRegistrationSuccessMessageExist();
        Assert.assertTrue(flag);
        registrationPage.clickOnLogoutLink();
    }
}
