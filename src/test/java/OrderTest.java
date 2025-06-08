
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OrderPage;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {
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


    @Test
    public void testOrderCreation() {
        System.out.printf("Запуск теста с параметрами: isTopButton=%b, metro=%s, period=%s%n",
                isTopButton, metro, period);
        if (isTopButton) {
            mainPage.clickOrderButtonOnTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(OrderPage.nameField));
        orderPage = new OrderPage(driver);
        orderPage.fillFirstPage(name, surname, address, metro, phone);
        orderPage.fillSecondPage(date, period, isBlack, comment);
        orderPage.confirmOrder();
        assertTrue("Модальное окно успешного заказа должно отображаться",
                orderPage.isSuccessModalDisplayed());

        String orderNumber = orderPage.getOrderNumber();
        assertNotNull("Номер заказа не должен быть null", orderNumber);
        assertFalse("Номер заказа не должен быть пустым", orderNumber.isEmpty());
        System.out.println("Успешно создан заказ №" + orderNumber);
    }

}