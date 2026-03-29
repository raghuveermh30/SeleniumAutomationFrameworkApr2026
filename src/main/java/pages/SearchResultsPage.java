package pages;

import constants.AppConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;

public class SearchResultsPage {

    WebDriver driver;
    private ElementUtil elementUtil;


    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    // Locators
    private final By searchResults = By.xpath("//div[@class = 'product-thumb']");


    //Methods
    @Step("getSearchResultsPageTitle")
    public String getSearchResultsPageTitle() {
        return driver.getTitle();
    }

    @Step("getProductResultsCount")
    public int getProductResultsCount() {
        elementUtil.waitForElementsToVisible(searchResults, AppConstants.SHORT_TIME_OUT);
        int count = elementUtil.getElements(searchResults).size();
        System.out.println("Products Results Page : " + count);
        return count;
    }

    @Step("selectProduct")
    public ProductInfoPage selectProduct(String productName) {
        System.out.println("Product Name is : " + productName);
        elementUtil.waitForElementsToBePresent(By.linkText(productName), AppConstants.MEDIUM_TIME_OUT);
        elementUtil.doClick(By.linkText(productName));
        return new ProductInfoPage(driver);
    }
}
