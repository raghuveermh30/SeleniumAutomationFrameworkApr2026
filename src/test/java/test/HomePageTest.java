package test;

import base.BaseTest;

import constants.AppConstants;
import constants.AppError;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;

@Epic("SUP-12346 - Home Page Design for OpenCart Application")
@Story("SUP-101 - Home Page Features for OpenCart Application")
@Feature("SUP-2 - Home Page Test Features")
public class HomePageTest extends BaseTest {

    @BeforeClass
    public void doLogin() {
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
    }

    @Description("Home Page Title Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void homePageTitleTest() {
        String homePageTitle = homePage.getHomePageTitle();
        Assert.assertEquals(homePageTitle, AppConstants.HOME_PAGE_TITLE, AppError.HOME_PAGE_TITLE_ERROR);
    }

    @Description("Home Page URL Test")
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void homePageUrlTest() {
        String homePageUrl = homePage.getHomePageUrl();
        Assert.assertTrue(homePageUrl.contains(AppConstants.HOME_PAGE_URL_FRACTION));
    }

    @Description("Checking the Existence of Logout Link on Home Page")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test
    public void logoutLinkDisplayed() {
        boolean flag = homePage.isLogoutLinkExist();
        Assert.assertTrue(flag);
    }

    @Description("Checking the Existence of My Account Link on Home Page")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test
    public void headersTest() {
        List<String> actualHeaders = homePage.getHeaders();
        System.out.println("Home Page Actual Headers : " + actualHeaders);
    }

    @DataProvider
    public Object[][] getProductSearchData() {
        return new Object[][]{
                {"macbook", 3},
                {"imac", 1},
                {"samsung", 2},
                {"canon", 1},
                {"airtel", 0}
        };
    }

    @Test(priority = Integer.MAX_VALUE, dataProvider = "getProductSearchData")
    public void doSearchTest(String searchKey, int resultCount) {
        searchResultsPage = homePage.doSearch(searchKey);
        System.out.println(searchResultsPage.getSearchResultsPageTitle());
        System.out.println("Product Results Count is : " + searchResultsPage.getProductResultsCount());
        Assert.assertEquals(searchResultsPage.getProductResultsCount(), resultCount);
    }

    @Test(description = "Logo Displayed Test", enabled = false)
    public void isLogoDisplayed() {
        Assert.assertTrue(commonPage.isLogoDisplayed(), AppError.LOGO_NOT_DISPLAYED_ERROR);
    }

    @Test(dataProvider = "getFooterData", description = "Footer Links Test", enabled = false)
    public void getFooterLink(String linkName) {
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
}
