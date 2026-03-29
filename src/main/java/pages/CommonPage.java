package pages;

import constants.AppConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementUtil;

import java.util.ArrayList;
import java.util.List;

public class CommonPage {

    WebDriver driver;
    private ElementUtil elementUtil;

    public CommonPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    private By logo = By.className("img-responsive");
    private By footer = By.xpath("//footer//a");

    public boolean isLogoDisplayed() {
        elementUtil.waitForElementVisible(logo, AppConstants.MAX_TIME_OUT);
        return elementUtil.doElementIsDisplayed(logo);
    }

    public List<String> getFooterLinks() {
        elementUtil.waitForElementsToBePresent(footer, AppConstants.MEDIUM_TIME_OUT);
        List<WebElement> footerLinks = elementUtil.getElements(footer);
        System.out.println("Total Number of footers : " + footerLinks.size());
        List<String> footers = new ArrayList<>();
        for (WebElement element : footerLinks) {
            String text = element.getText();
            footers.add(text);
        }
        return footers;
    }

    public boolean checkFooterLink(String footerLink) {
        return getFooterLinks().contains(footerLink);
    }
}
