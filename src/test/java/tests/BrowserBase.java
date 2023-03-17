package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import setup.BrowserFactory;
import setup.ScreenshotWatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

public class BrowserBase {
    static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    static JavascriptExecutor js;
    @RegisterExtension
    ScreenshotWatcher watcher = new ScreenshotWatcher((WebDriver) driver.get(), "target/surefire-reports");

    @BeforeAll
    public static void setUp() {
        driver.set(BrowserFactory.buildBrowser());
        js = (JavascriptExecutor) driver.get();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @BeforeEach
    public void loadPage() {
        driver.get().get("https://www.saucedemo.com");
    }

    @AfterAll
    public static void tearDown() {
        driver.get().quit();
    }


    //shared methods for testing
    //get title
    public String getPageTitle() {
        return driver.get().getTitle();
    }

    //scroll element into view
    public void scrollTo(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //screenshot
    public void takeScreenshot(String dir, String fileName) {
        try {
            new File(dir).mkdirs();
            try (FileOutputStream output = new FileOutputStream(dir + File.separator + "screenshot" + fileName + ".png")) {
                output.write(((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES));
            }
        } catch (IOException | WebDriverException ex) {
        }
    }
}
