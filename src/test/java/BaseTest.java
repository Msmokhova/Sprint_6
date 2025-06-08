import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import pages.AppConfig;
import pages.MainPage;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.OrderPage;

public class BaseTest {
    protected WebDriver driver;
    protected MainPage mainPage;
    protected OrderPage orderPage;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        //WebDriverManager.firefoxdriver().setup();
        //driver = new FirefoxDriver();
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
        driver.get(AppConfig.BASE_URL);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
