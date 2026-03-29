package pages;

import constants.AppConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ProductInfoPage {

    WebDriver driver;
    private ElementUtil elementUtil;
    private Map<String, String> productMap;

    public ProductInfoPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    //By Locators
    private final By productHeader = By.cssSelector("div.row > div > h1");
    private final By productDetails = By.xpath("//div[@class = 'col-sm-4']//ul[@class = 'list-unstyled']//li");
    private final By productImagesCount = By.xpath("//ul[@class = 'thumbnails']//li");
    private final By productMetaData = By.xpath("(//div[@id = 'content']//ul[@class = 'list-unstyled'])[1]//li");
    private final By productPriceData = By.xpath("(//div[@id = 'content']//ul[@class = 'list-unstyled'])[2]//li");

    private final By myAccount = By.xpath("//span[normalize-space()='My Account']");
    private final By myAccountList = By.xpath("//ul[@class = 'dropdown-menu dropdown-menu-right']/li");

    public String getProductHeader() {
        String header = elementUtil.doElementGetText(productHeader);
        System.out.println("Product Header : " + header);
        return header;
    }

    public LogoutPage logout() {
        elementUtil.doClick(myAccount);
        List<WebElement> myAccountDropDownList = driver.findElements(myAccountList);
        for (WebElement element : myAccountDropDownList) {
            if (element.getText().contains("Logout")) {
                element.click();
                break;
            }
        }
        return new LogoutPage(driver);
    }

    public List<String> getProductDetails() {
        List<WebElement> productDetailsList = elementUtil.getElements(productDetails);
        List<String> productDetailsValueList = productDetailsList.stream().map(e -> e.getText()).collect(Collectors.toList());
        productDetailsValueList.forEach(e -> System.out.println(e));
        return productDetailsValueList;
    }

    public int getProductImagesCount() {
        int imagesCount = elementUtil.waitForElementsToBePresent(productImagesCount, AppConstants.MEDIUM_TIME_OUT).size();
        System.out.println(getProductHeader() + " : Images count : " + imagesCount);
        return imagesCount;
    }

    public Map<String, String> getProductInfoDetails() {
        // productMap = new LinkedHashMap<>();
        productMap = new TreeMap<>();
        productMap.put("header", getProductHeader());
        productMap.put("imagesCount", getProductImagesCount() + "");
        getProductMetaData();
        getProductPriceData();

        return productMap;
    }

   /* Product Code: SAM1
    Reward Points: 1000
    Availability: Pre-Order*/

    private void getProductMetaData() {
        List<WebElement> productMetaList = elementUtil.waitForElementsToBePresent(productMetaData, AppConstants.MEDIUM_TIME_OUT);
        for (WebElement element : productMetaList) {
            String metaText = element.getText();
            String metaArray[] = metaText.split(":");
            String metaKey = metaArray[0].trim();
            String metaValue = metaArray[1].trim();
            productMap.put(metaKey, metaValue);
        }
    }

    /*  $241.99
      Ex Tax: $199.99*/
    private void getProductPriceData() {
        List<WebElement> productPriceList = elementUtil.waitForElementsToBePresent(productPriceData, AppConstants.MEDIUM_TIME_OUT);
        String productPrice = productPriceList.get(0).getText().trim();
        String productTaxPrice = productPriceList.get(1).getText().split(":")[1].trim();
        productMap.put("price", productPrice);
        productMap.put("priceTax", productTaxPrice);
    }

}
