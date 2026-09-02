package test;

import base.BaseTest;
import com.aventstack.chaintest.plugins.ChainTestListener;
import constants.AppConstants;
import constants.AppError;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * Test suite covering OpenCart product detail page functionality: header,
 * details, image counts (inline and Excel-driven data) and aggregated info.
 */
@Epic("SUP-12346 - Product Page Design for OpenCart Application")
@Story("SUP-101 - Product Page Features for OpenCart Application")
@Feature("SUP-2 - Product Page Test Features")
public class ProductInfoPageTest extends BaseTest {

    /**
     * Logs in before any product info test runs, populating {@code homePage}.
     */
    @BeforeClass
    public void productInfoSetup() {
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
    }

    /**
     * Verifies the product detail page header matches the selected product name.
     */
    @Description("Product Header Search Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void productHeaderSearchHeaderTest() {
        searchResultsPage = homePage.doSearch("macbook");
        productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
        String actualProductHeader = productInfoPage.getProductHeader();
        Assert.assertEquals(actualProductHeader, "MacBook Pro", "===Product Header is not Matched===");
    }

    /**
     * Verifies logging out from the product detail page shows the logout confirmation message.
     */
    @Description("Logout From Product Page")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(priority = Integer.MAX_VALUE)
    public void logoutFromProductPage() {
        searchResultsPage = homePage.doSearch("macbook");
        productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
        String actualProductHeader = productInfoPage.getProductHeader();
        Assert.assertEquals(actualProductHeader, "MacBook Pro", "===Product Header is not Matched===");
        logoutPage = productInfoPage.logout();
        Assert.assertTrue(logoutPage.isLogoutMsgDisplayed());
    }

    /**
     * Supplies search keyword / product name pairs for the product details test.
     *
     * @return an array of {searchKey, productName} pairs
     */
    @DataProvider
    public Object[][] getProductDetails() {
        return new Object[][]{
                {"macbook", "MacBook Pro"},
                {"macbook", "MacBook Air"},
                {"imac", "iMac"},
                {"samsung", "Samsung SyncMaster 941BW"},
                {"samsung", "Samsung Galaxy Tab 10.1"},
        };
    }

    /**
     * Verifies that product detail list items are populated for each product.
     *
     * @param searchKey the product keyword to search for
     * @param productName the exact product name to select from search results
     */
    @Description("Get Product Details")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(dataProvider = "getProductDetails")
    public void productDetailsTest(String searchKey, String productName) {
        searchResultsPage = homePage.doSearch(searchKey);
        productInfoPage = searchResultsPage.selectProduct(productName);
        List<String> productDetailsList = productInfoPage.getProductDetails();
        productDetailsList.forEach(System.out::println);
        Assert.assertFalse(productDetailsList.isEmpty(), AppError.PRODUCT_DETAILS_EMPTY_ERROR + " for product: " + productName);
    }

    /**
     * Supplies search keyword / product name / expected image count triples.
     *
     * @return an array of {searchKey, productName, expectedImageCount} triples
     */
    @DataProvider
    public Object[][] getProductImageData() {
        return new Object[][]{
                {"macbook", "MacBook Pro", 4},
                {"macbook", "MacBook Air", 4},
                {"imac", "iMac", 3},
                {"samsung", "Samsung SyncMaster 941BW", 1},
                {"samsung", "Samsung Galaxy Tab 10.1", 7},
        };
    }


    /**
     * Supplies product image count test data read from the Excel test data sheet.
     *
     * @return a 2D array of test data rows read from Excel
     */
    @DataProvider
    public Object[][] getProductImageSheetData() {
        return ExcelUtil.getTestData(AppConstants.TEST_DATA_WORKBOOK_PATH, AppConstants.PRODUCT_SHEET_NAME);
    }

    /**
     * Verifies the number of product images matches the expected count.
     *
     * @param searchProductKey the product keyword to search for
     * @param productName the exact product name to select from search results
     * @param expectedImageCount the expected number of product images
     */
    @Description("Get Product Image Data via Excel")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(dataProvider = "getProductImageData")
    public void productImagesCount(String searchProductKey, String productName, int expectedImageCount) {
        searchResultsPage = homePage.doSearch(searchProductKey);
        productInfoPage = searchResultsPage.selectProduct(productName);
        int actualProductImageCount = productInfoPage.getProductImagesCount();
        Assert.assertEquals(actualProductImageCount, expectedImageCount);
    }


    /**
     * Verifies the number of product images matches the expected count sourced from Excel.
     *
     * @param searchKey the product keyword to search for
     * @param productName the exact product name to select from search results
     * @param expectedImageCount the expected number of product images, as a string from Excel
     */
    @Test(dataProvider = "getProductImageSheetData")
    public void productImagesCountFromSheet(String searchKey, String productName, String expectedImageCount) {
        searchResultsPage = homePage.doSearch(searchKey);
        productInfoPage = searchResultsPage.selectProduct(productName);
        int actualProductImageCount = productInfoPage.getProductImagesCount();
        Assert.assertEquals(actualProductImageCount, Integer.parseInt(expectedImageCount));
    }

    /**
     * Verifies aggregated product info (brand, availability, reward points,
     * product code and pricing) matches expected values.
     */
    @Description("getProductInfoTest")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void getProductInfoTest() {
        searchResultsPage = homePage.doSearch("macbook");
        productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
        Map<String, String> productInfoMap = productInfoPage.getProductInfoDetails();
        productInfoMap.forEach((K, V) -> System.out.println(K + " : " + V));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(productInfoMap.get("Brand"), "Apple");
        softAssert.assertEquals(productInfoMap.get("Availability"), "Out Of Stock");
        softAssert.assertEquals(productInfoMap.get("Reward Points"), "800");
        softAssert.assertEquals(productInfoMap.get("Product Code"), "Product 18");

        //Price
        softAssert.assertEquals(productInfoMap.get("price"), "$2,000.00");
        softAssert.assertEquals(productInfoMap.get("priceTax"), "$2,000.00");
        softAssert.assertAll();
    }
}
