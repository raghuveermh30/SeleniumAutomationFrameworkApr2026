package pages;

import constants.AppConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;

/**
 * Page object representing the OpenCart product search results page.
 */
public class SearchResultsPage {

    WebDriver driver;
    private ElementUtil elementUtil;


    /**
     * Creates a new SearchResultsPage bound to the given driver.
     *
     * @param driver the WebDriver instance controlling the browser
     */
    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    // Locators
    private final By searchResults = By.xpath("//div[@class = 'product-thumb']");


    //Methods
    /**
     * Returns the search results page title.
     *
     * @return the page title
     */
    @Step("getSearchResultsPageTitle")
    public String getSearchResultsPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the number of product results displayed on the page.
     *
     * @return the count of product results
     */
    @Step("getProductResultsCount")
    public int getProductResultsCount() {
        elementUtil.waitForElementsToVisible(searchResults, AppConstants.SHORT_TIME_OUT);
        int count = elementUtil.getElements(searchResults).size();
        System.out.println("Products Results Page : " + count);
        return count;
    }

    /**
     * Clicks on the product link matching the given name and returns its detail page.
     *
     * @param productName the exact link text of the product to select
     * @return a new {@link ProductInfoPage}
     */
    @Step("selectProduct")
    public ProductInfoPage selectProduct(String productName) {
        System.out.println("Product Name is : " + productName);
        elementUtil.waitForElementsToBePresent(By.linkText(productName), AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(By.linkText(productName));
        return new ProductInfoPage(driver);
    }
}
