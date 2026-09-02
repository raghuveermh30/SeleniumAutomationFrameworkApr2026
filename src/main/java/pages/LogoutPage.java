package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;

/**
 * Page object representing the OpenCart account logout confirmation page.
 */
public class LogoutPage {

    WebDriver driver;
    private ElementUtil elementUtil;

    /**
     * Creates a new LogoutPage bound to the given driver.
     *
     * @param driver the WebDriver instance controlling the browser
     */
    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    private final By accountLogoutText = By.xpath("//h1[normalize-space()='Account Logout']");


    /**
     * Checks whether the "Account Logout" confirmation message is displayed.
     *
     * @return true if the logout message is displayed, false otherwise
     */
    public boolean isLogoutMsgDisplayed() {
        return elementUtil.isElementDisplayed(accountLogoutText);
    }




}
