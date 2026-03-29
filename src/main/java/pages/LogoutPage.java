package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ElementUtil;

public class LogoutPage {

    WebDriver driver;
    private ElementUtil elementUtil;

    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        elementUtil = new ElementUtil(driver);
    }

    private final By accountLogoutText = By.xpath("//h1[normalize-space()='Account Logout']");


    public boolean isLogoutMsgDisplayed() {
        return elementUtil.isElementDisplayed(accountLogoutText);
    }




}
