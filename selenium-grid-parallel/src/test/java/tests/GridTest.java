package tests;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import factory.GridDriverFactory;

public class GridTest {
    WebDriver driver;
    static ExtentReports extent;
    ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void setupExtent() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-reports/GridParallelExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
    }
    
    @AfterSuite(alwaysRun = true)
    public void tearDownExtent() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setup(String browser) throws Exception {
        test = extent.createTest("Test on :: " + browser);
        driver = GridDriverFactory.createDriver(browser);
    }

    @Test
    public void openGoogle() {
        driver.get("https://www.google.com");
        test.pass("Page title :: " + driver.getTitle());
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = takeScreenshot(result.getName());
            try {
                test.fail(result.getThrowable()).addScreenCaptureFromPath(screenshotPath);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
           // driver.quit();
        }
    }

    public String takeScreenshot(String testName) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + dateName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }
}
