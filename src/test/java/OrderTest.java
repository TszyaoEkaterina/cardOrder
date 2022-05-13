
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrderTest {
    WebDriver driver;
    
    @BeforeAll
    public static void setUpClass() {
        //System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }
    
    @Test
    void shouldSubmitRequest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Цзяо Екатерина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }
    
    @Test
    void shouldThrowErrorWithInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Kate");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] .input__inner .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }
    
    @Test
    void shouldThrowErrorWithInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Цзяо Екатерина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7123456789000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] .input__inner .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }
    
    @Test
    void shouldNotSubmitWithoutName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] .input__inner .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }
    
    @Test
    void shouldNotSubmitWithoutPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Цзяо Екатерина");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] .input__inner .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }
    
    @Test
    void shouldNotSubmitWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Цзяо Екатерина");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и " +
                "разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
    
    @AfterEach
    public void close() {
        driver.quit();
        driver = null;
    }
}
