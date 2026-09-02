package pages;

import constants.AppConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object representing the OpenCart account / home page.
 * <p>
 * Exposes actions available after a successful login, such as searching products
 * and logging out.
 */
public class HomePage {

    WebDriver driver;
    private ElementUtil elementUtil;

    /**
     * Creates a new HomePage bound to the given driver.
     *
     * @param driver the WebDriver instance controlling the browser
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }


    // By Locators
    //1. By Locators --> Object Repo
    private final By logoutLink = By.linkText("Logout");
    private final By headers = By.xpath("//div[@id='content']/h2");
    private final By search = By.name("search");
    private final By searchBtn = By.cssSelector("div#search button");

    //2. Page Actions
    /**
     * Returns the current page title.
     *
     * @return the home page title
     */
    @Step("Home Page Title")
    public String getHomePageTitle() {
        String title = elementUtil.waitForTitleContains(AppConstants.HOME_PAGE_TITLE, AppConstants.DEFAULT_TIME_OUT);
        System.out.println("Home Page Title is " + title);
        return title;
    }

    /**
     * Returns the current page URL.
     *
     * @return the home page URL
     */
    @Step("Home Page Url")
    public String getHomePageUrl() {
        String url = elementUtil.waitForUrlContains(AppConstants.HOME_PAGE_URL_FRACTION, AppConstants.DEFAULT_TIME_OUT);
        System.out.println("Home Page Url is " + url);
        return url;
    }

    /**
     * Checks whether the Logout link is present on the home page.
     *
     * @return true if the logout link exists, false otherwise
     */
    @Step("Logout Link is Displayed")
    public boolean isLogoutLinkExist() {
        return elementUtil.isElementDisplayed(logoutLink);
    }

    /**
     * Returns the text of all section headers displayed on the home page.
     *
     * @return a list of header texts
     */
    @Step("Get Headers on the Element")
    public List<String> getHeaders() {
        // driver.findElements(headers).stream().forEach(e -> System.out.println(e.getText()));
        List<WebElement> headersList = elementUtil.waitForElementsToVisible(headers, AppConstants.SHORT_TIME_OUT);

        List<String> headersValueList = new ArrayList<>();
        for (WebElement e : headersList) {
            String text = e.getText();
            System.out.println(text);
            headersValueList.add(text);
        }
        return headersValueList;
    }

    /**
     * Clicks the Logout link if it is displayed.
     */
    @Step("Logout Link is Displayed")
    public void logout() {
        if (isLogoutLinkExist()) {
            elementUtil.getElement(logoutLink).click();
        }
    }

    /**
     * Logs out from the application and returns the resulting Logout page.
     *
     * @return a new {@link LogoutPage}
     */
    @Step("Logout from Home Page and return LogoutPage")
    public LogoutPage doLogout() {
        if (isLogoutLinkExist()) {
            elementUtil.getElement(logoutLink).click();
        }
        return new LogoutPage(driver);
    }

    /**
     * Searches for the given product and returns the search results page.
     *
     * @param searchKey the product keyword to search for
     * @return a new {@link SearchResultsPage}
     */
    @Step("Search the Product : {0}")
    public SearchResultsPage doSearch(String searchKey) {
        System.out.println("My Search Key is " + searchKey);
        elementUtil.waitForElementVisible(search, AppConstants.MAX_TIME_OUT);
        elementUtil.doSendKeys(search, searchKey);
        elementUtil.waitForElementVisible(searchBtn, AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(searchBtn);
        return new SearchResultsPage(driver);
    }
}
