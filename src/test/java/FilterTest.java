import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class FilterTest {

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setupBrowser(){
        System.setProperty("webdriver.chrome.driver", "C:\\Prog\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
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
        Assert.assertEquals(pricesArray,pricesArray.stream().sorted().toList());

    }

    @AfterMethod(alwaysRun = true)
    public void quitBrowser(){
        driver.quit();
        driver = null;
    }



}
