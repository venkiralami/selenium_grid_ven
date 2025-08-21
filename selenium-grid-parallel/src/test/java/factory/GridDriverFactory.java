package factory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.net.URL;

public class GridDriverFactory {
    public static WebDriver createDriver(String browser) throws Exception {
        URL gridUrl = new URL("http://localhost:4444/wd/hub");
        if (browser.equalsIgnoreCase("chrome")) {
            return new RemoteWebDriver(gridUrl, new ChromeOptions());
        } else if (browser.equalsIgnoreCase("firefox")) {
            return new RemoteWebDriver(gridUrl, new FirefoxOptions());
        }
        throw new IllegalArgumentException("Unsupported browser:: " + browser);
    }
}
