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

/**
 * Test suite covering OpenCart account registration: successful registration
 * with Excel-driven data, and validation failures for empty/mismatched fields.
 */
public class RegistrationPageTest extends BaseTest {

    /**
     * Supplies user registration test data read from the Excel test data sheet.
     *
     * @return a 2D array of registration test data rows read from Excel
     */
    @DataProvider
    public Object[][] getRegistrationData() {
        return ExcelUtil.getTestData(AppConstants.REGISTRATION_SHEET_NAME);
    }

    /**
     * Verifies that registering a new user with valid data succeeds and shows
     * the success message, then logs out.
     *
     * @param firstName first name
     * @param lastName last name
     * @param email base email address
     * @param telephone telephone number
     * @param password account password
     * @param confirmpassword password confirmation
     */
    @Test(dataProvider = "getRegistrationData")
    public void userRegistrationTest(String firstName, String lastName, String email, String telephone, String password, String confirmpassword) {
        registrationPage = loginPage.clickOnRegister();
        registrationPage.doRegister(firstName, lastName, email, telephone, password, confirmpassword);
        boolean flag = registrationPage.isRegistrationSuccessMessageExist();
        Assert.assertTrue(flag);
        registrationPage.clickOnLogoutLink();
    }

    /**
     * Verifies registration fails with validation errors when required fields are left empty.
     */
    @Description("Registration should fail and show validation errors when required fields are empty")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1, description = "Registration with empty required fields")
    public void registrationWithEmptyFieldsTest() {
        DriverFactory.getDriver().navigate().to(properties.getProperty("url"));
        registrationPage = loginPage.clickOnRegister();
        registrationPage.submitWithEmptyFields();
        Assert.assertTrue(registrationPage.hasValidationErrors(), AppError.REGISTRATION_VALIDATION_ERROR + " for empty fields");
    }

    /**
     * Verifies registration fails with a validation error when the password
     * and confirmation password do not match.
     */
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
