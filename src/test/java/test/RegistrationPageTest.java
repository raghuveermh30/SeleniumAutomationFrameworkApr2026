package test;

import base.BaseTest;
import constants.AppConstants;
import constants.AppError;
import factory.DriverFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelUtil;

public class RegistrationPageTest extends BaseTest {

    @DataProvider
    public Object[][] getRegistrationData() {
        return ExcelUtil.getTestData(AppConstants.REGISTRATION_SHEET_NAME);
    }

    @Test(dataProvider = "getRegistrationData")
    public void userRegistrationTest(String firstName, String lastName, String email, String telephone, String password, String confirmpassword) {
        registrationPage = loginPage.clickOnRegister();
        registrationPage.doRegister(firstName, lastName, email, telephone, password, confirmpassword);
        boolean flag = registrationPage.isRegistrationSuccessMessageExist();
        Assert.assertTrue(flag);
        registrationPage.clickOnLogoutLink();
    }

    @Description("Registration should fail and show validation errors when required fields are empty")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1, description = "Registration with empty required fields")
    public void registrationWithEmptyFieldsTest() {
        DriverFactory.getDriver().navigate().to(properties.getProperty("url"));
        registrationPage = loginPage.clickOnRegister();
        registrationPage.submitWithEmptyFields();
        Assert.assertTrue(registrationPage.hasValidationErrors(), AppError.REGISTRATION_VALIDATION_ERROR + " for empty fields");
    }

    @Description("Registration should fail and show validation error when passwords do not match")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 2, description = "Registration with mismatched passwords")
    public void registrationWithMismatchedPasswordsTest() {
        DriverFactory.getDriver().navigate().to(properties.getProperty("url"));
        registrationPage = loginPage.clickOnRegister();
        registrationPage.doRegister("Test", "User", "mismatch@test.com", "9876543210", "Pass@123", "DifferentPass@456");
        Assert.assertTrue(registrationPage.hasValidationErrors(), AppError.REGISTRATION_VALIDATION_ERROR + " for mismatched passwords");
    }
}
