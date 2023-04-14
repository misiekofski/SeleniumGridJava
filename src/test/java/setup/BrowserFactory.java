package setup;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

public class BrowserFactory {
    private static WebDriver browserObject;
    private static URL gridAddress;

    public static enum browserType {
        FIREFOX, CHROME, EDGE
    }

    public static WebDriver buildBrowser() {

        String browser = System.getProperty("browser", "chrome").toLowerCase();
        switch (browser) {
            case "firefox" -> browserObject = BrowserFactory.buildBrowser(browserType.FIREFOX);
            case "edge" -> browserObject = BrowserFactory.buildBrowser(browserType.EDGE);
            default -> browserObject = BrowserFactory.buildBrowser(browserType.CHROME);
        }

        return browserObject;
    }

    public static WebDriver buildBrowser(browserType name) {
        MutableCapabilities options = null;
        String gridProperty = System.getProperty("grid", "http://localhost:4444");
        try {
            gridAddress = new URL(gridProperty);
        } catch (MalformedURLException e) {
        }

        options = switch (name) {
            case CHROME -> new ChromeOptions().addArguments("--remote-allow-origins=*");
            case FIREFOX -> new FirefoxOptions();
            case EDGE -> new EdgeOptions().addArguments("--remote-allow-origins=*");
        };

        if (gridAddress != null) {
            browserObject = RemoteWebDriver.builder().oneOf(options).address(gridAddress).build();
        } else {
            browserObject = RemoteWebDriver.builder().oneOf(options).build();
        }

        return browserObject;
    }

}

