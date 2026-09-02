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

/**
 * Test suite covering OpenCart home page functionality: title/URL checks,
 * headers, logout link presence, product search, logo and footer links.
 */
@Epic("SUP-12346 - Home Page Design for OpenCart Application")
@Story("SUP-101 - Home Page Features for OpenCart Application")
@Feature("SUP-2 - Home Page Test Features")
public class HomePageTest extends BaseTest {

    /**
     * Logs in before any home page test runs, populating {@code homePage}.
     */
    @BeforeClass
    public void doLogin() {
        homePage = loginPage.doLogin(properties.getProperty("username"), properties.getProperty("password"));
    }

    /**
     * Verifies the home page title matches the expected value.
     */
    @Description("Home Page Title Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void homePageTitleTest() {
        String homePageTitle = homePage.getHomePageTitle();
        Assert.assertEquals(homePageTitle, AppConstants.HOME_PAGE_TITLE, AppError.HOME_PAGE_TITLE_ERROR);
    }

    /**
     * Verifies the home page URL contains the expected fragment.
     */
    @Description("Home Page URL Test")
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("Raghuveer Hanumantharaya")
    @Test
    public void homePageUrlTest() {
        String homePageUrl = homePage.getHomePageUrl();
        Assert.assertTrue(homePageUrl.contains(AppConstants.HOME_PAGE_URL_FRACTION));
    }

    /**
     * Verifies the Logout link is present on the home page.
     */
    @Description("Checking the Existence of Logout Link on Home Page")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test
    public void logoutLinkDisplayed() {
        boolean flag = homePage.isLogoutLinkExist();
        Assert.assertTrue(flag);
    }

    /**
     * Verifies the home page section headers are populated after login.
     */
    @Description("Checking the Existence of My Account Link on Home Page")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Naveen Automation Labs")
    @Test
    public void headersTest() {
        List<String> actualHeaders = homePage.getHeaders();
        System.out.println("Home Page Actual Headers : " + actualHeaders);
        Assert.assertFalse(actualHeaders.isEmpty(), AppError.HOME_PAGE_HEADERS_EMPTY_ERROR);
    }

    /**
     * Supplies product search keywords along with the expected result counts.
     *
     * @return an array of {searchKey, expectedResultCount} pairs
     */
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

    /**
     * Verifies the search results page title contains the expected fragment.
     */
    @Description("Search Results Page Title Test")
    @Severity(SeverityLevel.MINOR)
    @Owner("Raghuveer Hanumantharaya")
    @Test(priority = 1, description = "Search results page title should contain 'Search'")
    public void searchResultsPageTitleTest() {
        searchResultsPage = homePage.doSearch("macbook");
        String title = searchResultsPage.getSearchResultsPageTitle();
        System.out.println("Search Results Page Title : " + title);
        Assert.assertTrue(title.contains(AppConstants.SEARCH_RESULTS_PAGE_TITLE_FRACTION),
                "Search results page title should contain '" + AppConstants.SEARCH_RESULTS_PAGE_TITLE_FRACTION + "'");
    }

    /**
     * Verifies that searching for each keyword returns the expected number of products.
     *
     * @param searchKey the product keyword to search for
     * @param resultCount the expected number of search results
     */
    @Test(priority = Integer.MAX_VALUE, dataProvider = "getProductSearchData")
    public void doSearchTest(String searchKey, int resultCount) {
        searchResultsPage = homePage.doSearch(searchKey);
        System.out.println(searchResultsPage.getSearchResultsPageTitle());
        System.out.println("Product Results Count is : " + searchResultsPage.getProductResultsCount());
        Assert.assertEquals(searchResultsPage.getProductResultsCount(), resultCount);
    }

    /**
     * Verifies the site logo is displayed. Currently disabled.
     */
    @Test(description = "Logo Displayed Test", enabled = false)
    public void isLogoDisplayed() {
        Assert.assertTrue(commonPage.isLogoDisplayed(), AppError.LOGO_NOT_DISPLAYED_ERROR);
    }

    /**
     * Verifies each expected footer link is present. Currently disabled.
     *
     * @param linkName the footer link text to check for
     */
    @Test(dataProvider = "footerLinksData", dataProviderClass = utils.TestDataUtil.class,
            description = "Footer Links Test", enabled = false)
    public void getFooterLink(String linkName) {
        List<String> footerLinks = commonPage.getFooterLinks();
        footerLinks.forEach(System.out::println);
        Assert.assertTrue(commonPage.checkFooterLink(linkName));
    }
}
