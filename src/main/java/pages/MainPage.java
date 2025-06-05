package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MainPage {
    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }
    private final By[] accordionButtons = {
            By.id("accordion__heading-0"),
            By.id("accordion__heading-1"),
            By.id("accordion__heading-2"),
            By.id("accordion__heading-3"),
            By.id("accordion__heading-4"),
            By.id("accordion__heading-5"),
            By.id("accordion__heading-6"),
            By.id("accordion__heading-7")
    };
    private final By orderButtonTop = By.cssSelector(".Button_Button__ra12g");
    private final By orderButtonBottom = By.cssSelector(".Button_Button__ra12g.Button_Middle__1CSJM");
    private final By confirmCookie = By.id("rcc-confirm-button");

    public String getAccordionPanelText(int index) {
        driver.findElement(confirmCookie).click();
        if (index < 0 || index >= accordionButtons.length) {
            throw new IllegalArgumentException("Invalid accordion index: " + index);
        }
        var element = driver.findElement(accordionButtons[index]);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(accordionButtons[index]));
        element.click();
        By panelContent = By.id("accordion__panel-" + index);
        wait.until(ExpectedConditions.visibilityOfElementLocated(panelContent));
        return driver.findElement(panelContent).getText();
    }


    public void clickOrderButtonOnTop() {
        driver.findElement(confirmCookie).click();
            driver.findElement(orderButtonTop).click();
        }

    public void clickOrderButtonBottom() {
        driver.findElement(confirmCookie).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(orderButtonBottom));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
}

