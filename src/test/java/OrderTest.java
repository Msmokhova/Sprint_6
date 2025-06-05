import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.OrderPage;


import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    private final boolean isTopButton;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String date;
    private final String period;
    private final boolean isBlack;
    private final String comment;

    public OrderTest(boolean isTopButton, String name, String surname, String address,
                     String metro, String phone, String date, String period,
                     boolean isBlack, String comment) {
        this.isTopButton = isTopButton;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.isBlack = isBlack;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "{index}: isTopButton={0}, metro={4}, period={7}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {true, "Мохова", "Мария", "ул. Ленина, 1", "Черкизовская", "89991112233", "01.07.2025", "сутки", true, "Тестовый заказ 1"},
                {false, "Мохова", "Мария", "ул. Пушкина, 10", "Сокольники", "89994445566", "02.07.2025", "двое суток", false, "Тестовый заказ 2"}
        });
    }


    @Before
    public void setup() {
        //WebDriverManager.firefoxdriver().setup();
        //driver = new FirefoxDriver();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> ((JavascriptExecutor)d)
                        .executeScript("return document.readyState").equals("complete"));
    }

    @Test
    public void testOrderCreation() {
        System.out.printf("Запуск теста с параметрами: isTopButton=%b, metro=%s, period=%s%n", isTopButton, metro, period);

        if (isTopButton) {
            mainPage.clickOrderButtonOnTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//input[@placeholder='* Имя']")));

        orderPage = new OrderPage(driver);
        orderPage.fillFirstPage(name, surname, address, metro, phone);
        orderPage.fillSecondPage(date, period, isBlack, comment);
        orderPage.confirmOrder();

        assertTrue("Success modal should be displayed", orderPage.isSuccessModalDisplayed());
        try {
            String orderNumber = orderPage.getOrderNumber();
            assertNotNull("Номер заказа не должен быть null", orderNumber);
            assertFalse("Номер заказа не должен быть пустым", orderNumber.isEmpty());
        } catch (RuntimeException e) {
            fail("Ошибка при получении номера заказа: " + e.getMessage());
        }
    }

    @After
    public void teardown() {
        driver.quit();
    }
}