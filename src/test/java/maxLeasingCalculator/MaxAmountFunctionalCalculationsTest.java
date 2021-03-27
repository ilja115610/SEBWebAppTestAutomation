package maxLeasingCalculator;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MaxAmountFunctionalCalculationsTest {

    private WebDriver driver;
    private static final String path = "src/main/resources/chromedriver/chromedriver.exe";
    private static final String chromeDriver = "webdriver.chrome.driver";
    private MaxLeasingCalculator calculator;
    private Map<String, WebElement> elementMap = new HashMap<>();
    private int income;
    private int liabilities;
    private int dependents;


    @BeforeAll
    private void setup() {
        driver = getDriver();
        calculator = new MaxAmount();
        openAndMaximize();
        WebElement income = driver.findElement(By.xpath("//*[@id=\"netoIncome\"]"));
        WebElement dependents = driver.findElement(By.xpath("//*[@id=\"numOfDependants\"]"));
        WebElement liabilities = driver.findElement(By.xpath("//*[@id=\"monthlyFinancialObligations\"]"));
        elementMap.put("Income",income);
        elementMap.put("Dependents",dependents);
        elementMap.put("Liabilities",liabilities);

    }

    @AfterAll
    public void cleanup() {
        driver.quit();
    }

    @AfterEach
    public void clearAfter () {
        for(Map.Entry<String,WebElement> entry : elementMap.entrySet()){
            entry.getValue().clear();
        }
    }

    @BeforeEach
    public void clearBefore () {
        for(Map.Entry<String,WebElement> entry : elementMap.entrySet()){
            entry.getValue().clear();
        }
    }

    public WebDriver getDriver() {
        System.setProperty(chromeDriver, path);
        return new ChromeDriver();
    }


    @Test
    public void incomeRangeTest () {

        dependents = 0; liabilities = 0;
         elementMap.get("Dependents").sendKeys(String.valueOf(dependents));
         elementMap.get("Liabilities").sendKeys(String.valueOf(liabilities));


        int actualResult;
        int expectedResult;
        for(int i = 1200; i<= 3000; i+=200){
            elementMap.get("Income").sendKeys(String.valueOf(i));
            WebElement result = driver.findElement(By.xpath("//*[@id=\"leaseSum\"]"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute('style')", result);
            actualResult = Integer.parseInt(result.getText().replaceAll(" ",""));
            expectedResult = calculator.getMaxAmount(i,dependents,liabilities);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Income").clear();
        }
    }


    @Test
    public void liabilitiesRangeTest () {

        income = 1200; dependents = 0;
        elementMap.get("Dependents").sendKeys(String.valueOf(dependents));
        elementMap.get("Income").sendKeys(String.valueOf(income));

        int actualResult;
        int expectedResult;
        for (int i = 10; i <= 300; i += 20) {
            elementMap.get("Liabilities").sendKeys(String.valueOf(i));
            WebElement result = driver.findElement(By.xpath("//*[@id=\"leaseSum\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('style')", result);
            actualResult = Integer.parseInt(result.getText().replaceAll(" ", ""));
            expectedResult = calculator.getMaxAmount(income, dependents, i);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Liabilities").clear();
        }
    }

    @Test
    public void dependentsRangeTest () {

        income = 1200; liabilities = 0;
        elementMap.get("Income").sendKeys(String.valueOf(income));
        elementMap.get("Liabilities").sendKeys(String.valueOf(liabilities));

        int actualResult;
        int expectedResult;
        for (int i = 0; i <= 4; i ++) {
            elementMap.get("Dependents").sendKeys(String.valueOf(i));
            WebElement result = driver.findElement(By.xpath("//*[@id=\"leaseSum\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('style')", result);
            actualResult = Integer.parseInt(result.getText().replaceAll(" ", ""));
            expectedResult = calculator.getMaxAmount(income, i, liabilities);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Dependents").clear();
        }

    }


    @Test
    public void dependentsRangeExceedTest () {

        income = 1200; liabilities = 0;
        elementMap.get("Income").sendKeys(String.valueOf(income));
        elementMap.get("Liabilities").sendKeys(String.valueOf(liabilities));

        for (int i = 5; i <= 8; i ++) {
            elementMap.get("Dependents").sendKeys(String.valueOf(i));
            WebElement result = driver.findElement(By.xpath("//*[@id=\"resultWrapperTextThree\"]/div/p"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('style')", result);
            assertThat(result.getText()).isEqualTo("We cannot provide financing with the entered data. Add a surety, if possible.");
            elementMap.get("Dependents").clear();
        }
    }


    @Test
    public void liabilitiesRangeExceedTest () {

        income = 1200; dependents = 0;
        elementMap.get("Income").sendKeys(String.valueOf(income));
        elementMap.get("Dependents").sendKeys(String.valueOf(dependents));

        int expectedResult;
        for (int i = 200; i <= 350; i +=20) {
            elementMap.get("Liabilities").sendKeys(String.valueOf(i));
            WebElement resultNum = driver.findElement(By.xpath("//*[@id=\"leaseSum\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('style')", resultNum);
            if(i<302){
                expectedResult = calculator.getMaxAmount(income, dependents, i);
                assertThat(Integer.parseInt(resultNum.getText().replaceAll(" ", ""))).isEqualTo(expectedResult);
                elementMap.get("Liabilities").clear();
                continue;
            }
            WebElement resultMsg = driver.findElement(By.xpath("//*[@id=\"resultWrapperTextFive\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('style')", resultMsg);
            assertThat(resultMsg.getText()).isEqualTo("With the entered data, the maximum lease amount is below 5,000 euros. If possible, add a surety or review SEB car loan.");
            elementMap.get("Liabilities").clear();
        }
    }



    public void openAndMaximize() {
        driver.get("https://www.seb.ee/eng/loan-and-leasing/leasing/car-leasing#calculators");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/ul/li[1]/a")).click();


    }

}
