import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WindowsTest {
    private String login = "diveroj856@htoal.com";
    private String password = "!Diveroj856";
    Logger log = LogManager.getLogger(WindowsTest.class);
    WebDriver driver;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void start(TestInfo info) {
        ChromeOptions options = new ChromeOptions();
        Set<String> tags = info.getTags();
        for (String tag : tags) {
            switch (tag.toLowerCase()) {
                case "headless":
                    options.addArguments("--headless", "--window-size=1920,1200", "--ignore-certificate-errors", "--silent");
                    driver = new ChromeDriver(options);
                    log.info("Открытие Chrome в headless режиме");
                    break;
                case "fullscreen":
                    options.addArguments("--start-fullscreen");
                    driver = new ChromeDriver(options);
                    //driver.manage().window().fullscreen();
                    log.info("Открытие Chrome в режиме киоска/fullscreen");
                    break;
                case "maximize":
                    options.addArguments("--start-maximized");
                    driver = new ChromeDriver(options);
                    //driver.manage().window().maximize();
                    log.info("Открытие Chrome в режиме полного экрана");
                    break;
                default:
                    driver = new ChromeDriver();
                    break;
            }
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterEach
    public void shutdown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    @Tag(value = "headless")
    public void headlessTest() {
        driver.get("https://duckduckgo.com/");
        log.info("Переход по ссылке");
        driver.findElement(By.cssSelector(".searchbox_input__bEGm3")).sendKeys("ОТУС", Keys.ENTER);
        log.info("Ввод ОТУС в поисковую строку");
        String expectedStr = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        String actualStr = driver.findElement(By.xpath("//span[contains(text(),'Онлайн‑курсы для профессионалов, дистанционное обучение современным ...')]")).getText();
        Assertions.assertEquals(expectedStr, actualStr);
        log.info("Осуществили проверку, что в поисковой выдаче первый результат Онлайн‑курсы для профессионалов, дистанционное обучение современным ...");
    }
    @Test
    @Tag(value = "fullscreen")
    public void fullscreenTest() {
        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
        log.info("Переход по ссылке");
        driver.findElement(By.cssSelector(".portfolio-item2.content")).click();
        log.info("Нажатие на картинку");
        WebElement img = driver.findElement(By.cssSelector(".pp_pic_holder.light_rounded"));
        if (img.isDisplayed()) {
            log.info("Картинка открылась в модальном окне");
        } else {
            log.info("Картинка не открылась в модальном окне.");
        }
    }
    @Test
    @Tag(value = "maximize")
    public void maximizeTest() {
        driver.get("https://otus.ru");
        log.info("Переход по ссылке");
        loginInOtus();
        log.info("Авторизация на https://otus.ru");
        log.info(driver.manage().getCookies());
        log.info("Вывод в лог все cookies");
    }
    private void loginInOtus() {
        driver.findElement(By.cssSelector(".sc-mrx253-0.enxKCy.sc-945rct-0.iOoJwQ")).click();
        clearAndEnter(By.cssSelector("input[name=\"email\"]"), login);
        clearAndEnter(By.cssSelector("input[type=\"password\"]"), password);
        driver.findElement(By.xpath("//div[contains(text(),'Войти')]")).click();
    }
    private void clearAndEnter(By by, String text) {
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }
}
