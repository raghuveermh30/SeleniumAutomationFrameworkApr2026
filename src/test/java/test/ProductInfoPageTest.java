package test;

import base.BaseTest;
import com.aventstack.chaintest.plugins.ChainTestListener;
import constants.AppConstants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import utils.ExcelUtil;

import java.util.List;
import java.util.Map;

@Epic("SUP-12346 - Product Page Design for OpenCart Application")
@Story("SUP-101 - Product Page Features for OpenCart Application")
@Feature("SUP-2 - Product Page Test Features")
public class ProductInfoPageTest extends BaseTest {

    @BeforeClass
    public void productInfoSetup() {
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
    }

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

    @Description("Get Product Details")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(dataProvider = "getProductDetails")
    public void productDetailsTest(String searchKey, String productName) {
        searchResultsPage = homePage.doSearch(searchKey);
        productInfoPage = searchResultsPage.selectProduct(productName);
        List<String> productDetailsList = productInfoPage.getProductDetails();
        productDetailsList.forEach(System.out::println);
    }

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


    @DataProvider
    public Object[][] getProductImageSheetData() {
        return ExcelUtil.getTestData(AppConstants.PRODUCT_SHEET_NAME);
    }

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


    @Test(dataProvider = "getProductImageSheetData")
    public void productImagesCountFromSheet(String searchKey, String productName, String expectedImageCount) {
        searchResultsPage = homePage.doSearch(searchKey);
        productInfoPage = searchResultsPage.selectProduct(productName);
        int actualProductImageCount = productInfoPage.getProductImagesCount();
        Assert.assertEquals(actualProductImageCount, Integer.parseInt(expectedImageCount));
    }

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
