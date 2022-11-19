import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterTest {

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setupBrowser() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://localhost:4444"),options);
    }

    @Test
    public void testSortAscendingByPrice(){
        driver.get("https://store.vaporesso.com/collections/collections");
        WebElement sortValue = driver.findElement(By.xpath("//*[@id=\"SortBy\"]"));
        Select select = new Select(sortValue);
        select.selectByValue("price-ascending");
        sortValue.submit();
        List<WebElement> pricesSale = driver.findElements(By.className("price-item--sale"));
        List<WebElement> pricesRegular = driver.findElements(By.className("price-item--regular"));
        List<Double> pricesArray = new ArrayList<>();
        List<Double> regularPricesArray = new ArrayList<>();
        for (var price : pricesRegular){
            if(!price.getText().isEmpty()){
                regularPricesArray.add(Double.valueOf(price.getText().replaceAll("[^\\d.]", "")));
            }
        }

        for(int i = 0; i < regularPricesArray.size(); i++){
            if(!pricesSale.get(i).getText().isEmpty()){
                pricesArray.add(Double.valueOf(pricesSale.get(i).getText().replaceAll("[^\\d.]", "")));
            } else {
                pricesArray.add(Double.valueOf(regularPricesArray.get(i)));
            }
        }
        Assert.assertEquals(pricesArray,pricesArray.stream().sorted().collect(Collectors.toList()));

    }

    @AfterMethod(alwaysRun = true)
    public void quitBrowser(){
        driver.quit();
        driver = null;
    }



}
