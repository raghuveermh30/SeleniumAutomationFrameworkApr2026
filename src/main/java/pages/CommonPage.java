package pages;

import constants.AppConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ElementUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object holding actions and locators common to multiple pages,
 * such as the site logo and footer links.
 */
public class CommonPage {

    WebDriver driver;
    private ElementUtil elementUtil;

    /**
     * Creates a new CommonPage bound to the given driver.
     *
     * @param driver the WebDriver instance controlling the browser
     */
    public CommonPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    private By logo = By.className("img-responsive");
    private By footer = By.xpath("//footer//a");

    /**
     * Checks whether the site logo is displayed on the page.
     *
     * @return true if the logo is visible, false otherwise
     */
    public boolean isLogoDisplayed() {
        elementUtil.waitForElementVisible(logo, AppConstants.MAX_TIME_OUT);
        return elementUtil.doElementIsDisplayed(logo);
    }

    /**
     * Returns the text of all footer links present on the page.
     *
     * @return a list of footer link texts
     */
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

    /**
     * Checks whether the given link text is present among the footer links.
     *
     * @param footerLink the footer link text to look for
     * @return true if the link is found, false otherwise
     */
    public boolean checkFooterLink(String footerLink) {
        return getFooterLinks().contains(footerLink);
    }
}
