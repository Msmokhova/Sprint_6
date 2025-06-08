package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class OrderPage {
    private final WebDriver driver;


    // Локаторы формы заказа
    public static final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.cssSelector("input.select-search__input");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // Локаторы второй части формы
    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.xpath("//div[contains(@class, 'Dropdown-control')]");
    private final By colorBlack = By.id("black");
    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[contains(@class, 'Button_Middle') and text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successModal = By.className("Order_ModalHeader__3FDaJ");

    // Локаторы модального окна подтверждения
    private final By orderNumberLocator = By.xpath("//div[contains(@class, 'Order_Text__2broi') and contains(text(), 'Номер заказа:')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillFirstPage(String name, String surname, String address, String metro, String phone) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);
        selectMetroStation(metro);
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(nextButton).click();

}
    private void selectMetroStation(String metro) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement metroInput = wait.until(ExpectedConditions.visibilityOfElementLocated(metroField));
        metroInput.click();
        metroInput.clear();
        metroInput.sendKeys(metro);
        By optionLocator = By.xpath("//button[contains(@class, 'select-search__option') and .//div[contains(@class, 'Order_Text__2broi') and text()='" + metro + "']]");
        WebElement stationOption = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        stationOption.click();
    }

    public void fillSecondPage(String date, String period, boolean isBlack, String comment) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
        WebElement dateInput = driver.findElement(dateField);
        dateInput.click();
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.RETURN);
        selectRentalPeriod(period);
        driver.findElement(By.xpath(String.format("//div[text()='%s']", period))).click();
        if (isBlack) {
            driver.findElement(colorBlack).click();
        }
        driver.findElement(commentField).sendKeys(comment);
        driver.findElement(orderButton).click();
    }

    public void selectRentalPeriod(String period) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement periodDropdown = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodField));
        periodDropdown.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Dropdown-menu")));
        By periodOptionLocator = By.xpath(String.format(".//div[@class='Dropdown-option' and text()='%s']", period));
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(periodOptionLocator));
        option.click();
    }
    public String getOrderNumber() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
            WebElement orderNumberElement = wait.until(ExpectedConditions.presenceOfElementLocated(orderNumberLocator));
            return orderNumberElement.getText().replaceAll("[^0-9]", "");
        } catch (TimeoutException e) {
            throw new RuntimeException("Не удалось получить номер заказа: элемент не появился в течение 15 секунд", e);
        }
    }

    public void confirmOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(confirmButton));
        driver.findElement(confirmButton).click();
    }

    public boolean isSuccessModalDisplayed() {
        List<WebElement> modals = driver.findElements(successModal);
        return !modals.isEmpty() &&
                modals.get(0).isDisplayed() &&
                modals.get(0).getText().contains("Заказ оформлен");
    }
}
