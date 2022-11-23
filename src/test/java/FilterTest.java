import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterTest {

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setupBrowser() throws MalformedURLException {
        String username = "eo.ursuz";
        String accessKey = "JRkb6gTT7gb776yGEmit9yS1Nkshtx0qtVdwT9L57PZGqxBKFr";
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("version", "92.0");
        capabilities.setCapability("platform", "Windows 10");
        capabilities.setCapability("resolution","1920x1080");
        capabilities.setCapability("build", "First Test");
        capabilities.setCapability("name", "Sample Test");
        capabilities.setCapability("network", true); // To enable network logs
        capabilities.setCapability("visual", true); // To enable step by step screenshot
        capabilities.setCapability("video", true); // To enable video recording
        capabilities.setCapability("console", true); // To capture console logs
        driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub"), capabilities);
//        driver = new ChromeDriver();
//        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void testSortAscendingByPrice() throws InterruptedException {
        driver.get("https://store.vaporesso.com/collections/collections?sort_by=price-ascending");
        List<WebElement> pricesSale = driver.findElements(By.className("price-item--sale"));
        List<WebElement> pricesRegular = driver.findElements(By.className("price-item--regular"));
        List<Double> pricesArray = new ArrayList<>();
        List<Double> regularPricesArray = new ArrayList<>();
        for (var price : pricesRegular){
            if(!price.getText().isBlank()){
                regularPricesArray.add(Double.valueOf(price.getText().replaceAll("[^\\d.]", "")));
            }
        }

        for(int i = 0; i < regularPricesArray.size(); i++){
            if(!pricesSale.get(i).getText().isBlank()){
                pricesArray.add(Double.valueOf(pricesSale.get(i).getText().replaceAll("[^\\d.]", "")));
            } else {
                pricesArray.add(regularPricesArray.get(i));
            }
        }
        System.out.println(pricesArray);
        Assert.assertEquals(pricesArray,pricesArray.stream().sorted().collect(Collectors.toList()));

    }

    @AfterMethod(alwaysRun = true)
    public void quitBrowser(){
        driver.quit();
        driver = null;
    }



}
