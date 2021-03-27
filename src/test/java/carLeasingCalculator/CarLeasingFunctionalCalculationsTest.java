package carLeasingCalculator;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarLeasingFunctionalCalculationsTest {

    private WebDriver driver;
    private static final String path = "src/main/resources/chromedriver/chromedriver.exe";
    private static final String chromeDriver = "webdriver.chrome.driver";
    private CarLeasingCalculator calculator;
    private Map<String, WebElement> elementMap = new HashMap<>();
    private double priceOfVehicle;
    private double depositPercent;
    private int periodInMonths;
    private double interestPercent;
    private double residualPercent;


    @BeforeAll
    private void setup() {
        driver = getDriver();
        calculator = new MonthlyPayment();
        openAndMaximize();
        WebElement price = driver.findElement(By.xpath("//*[@id=\"calc08-sum\"]"));
        WebElement deposit = driver.findElement(By.xpath("//*[@id=\"calc08-deposit\"]"));
        WebElement interest = driver.findElement(By.xpath("//*[@id=\"calc08-int\"]"));
        WebElement residual = driver.findElement(By.xpath("//*[@id=\"calc08-salvage-value\"]"));
        elementMap.put("Price",price);
        elementMap.put("Deposit",deposit);
        elementMap.put("Interest",interest);
        elementMap.put("Residual",residual);

    }

    @AfterAll
    public void cleanup() {
        driver.quit();
    }

    @AfterEach
    public void clear () {
        for(Map.Entry<String,WebElement> entry : elementMap.entrySet()){
            entry.getValue().clear();
        }
    }

    public WebDriver getDriver() {
        System.setProperty(chromeDriver, path);
        return new ChromeDriver();
    }

    /**
     * This method tests calculations with selected range of prices (10000-20000)
     * and other variables (downpayment, period, interest..) are fixed.
     */

    @Test
    public void priceRangeTest () {

        depositPercent = 20; periodInMonths = 60; interestPercent = 3.5; residualPercent = 25;
        elementMap.get("Deposit").sendKeys(String.valueOf(depositPercent));
        Select period = new Select(driver.findElement(By.xpath("//*[@id=\"calc08-period\"]")));
        period.selectByVisibleText(String.valueOf(periodInMonths));
        elementMap.get("Interest").clear(); elementMap.get("Interest").sendKeys(String.valueOf(interestPercent));
        elementMap.get("Residual").clear(); elementMap.get("Residual").sendKeys(String.valueOf(residualPercent));

        double actualResult;
        double expectedResult;
        for(int i = 10000; i <=20000; i+=500){
            elementMap.get("Price").sendKeys(String.valueOf(i));
            WebElement res = driver.findElement(By.xpath("//*[@id=\"monthly-result\"]/span/i"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute('style')", res);
            actualResult = Double.parseDouble(res.getText());
            expectedResult = calculator.calculateMonthlyPayment(i,depositPercent,periodInMonths,interestPercent,residualPercent);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Price").clear();
        }

    }

    /**
     * This method tests range of deposits from 10% - 70% with other variables fixed.
     */
    @Test
    public void depositRangeTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; residualPercent = 25;
        elementMap.get("Price").sendKeys(String.valueOf(priceOfVehicle));
        Select period = new Select(driver.findElement(By.xpath("//*[@id=\"calc08-period\"]")));
        period.selectByVisibleText(String.valueOf(periodInMonths));
        elementMap.get("Interest").clear(); elementMap.get("Interest").sendKeys(String.valueOf(interestPercent));
        elementMap.get("Residual").clear(); elementMap.get("Residual").sendKeys(String.valueOf(residualPercent));

        double actualResult;
        double expectedResult;
        for(int i = 10; i <=70; i+=5){
            elementMap.get("Deposit").sendKeys(String.valueOf(i));
            WebElement res = driver.findElement(By.xpath("//*[@id=\"monthly-result\"]/span/i"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute('style')", res);
            actualResult = Double.parseDouble(res.getText());
            expectedResult = calculator.calculateMonthlyPayment(priceOfVehicle,i,periodInMonths,interestPercent,residualPercent);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Deposit").clear();
        }
    }

    /**
     * This method tests range of residual values 10%-70% with other variables fixed.
     */
    @Test
    public void residualRangeTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; depositPercent = 20;
        elementMap.get("Price").sendKeys(String.valueOf(priceOfVehicle));
        elementMap.get("Deposit").sendKeys(String.valueOf(depositPercent));
        Select period = new Select(driver.findElement(By.xpath("//*[@id=\"calc08-period\"]")));
        period.selectByVisibleText(String.valueOf(periodInMonths));
        elementMap.get("Interest").clear(); elementMap.get("Interest").sendKeys(String.valueOf(interestPercent));
        elementMap.get("Residual").clear();

        double actualResult;
        double expectedResult;
        for(int i = 10; i <=70; i+=5){
            elementMap.get("Residual").sendKeys(String.valueOf(i));
            WebElement res = driver.findElement(By.xpath("//*[@id=\"monthly-result\"]/span/i"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].removeAttribute('style')", res);
            actualResult = Double.parseDouble(res.getText());
            expectedResult = calculator.calculateMonthlyPayment(priceOfVehicle,depositPercent,periodInMonths,interestPercent,i);
            assertThat(actualResult).isEqualTo(expectedResult);
            elementMap.get("Residual").clear();
        }
    }

    /**
     * Negative testing of deposit amount
     */
    @Test
    public void depositMoreThanPriceTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; depositPercent = 101;residualPercent = 25;
        elementMap.get("Price").sendKeys(String.valueOf(priceOfVehicle));
        Select period = new Select(driver.findElement(By.xpath("//*[@id=\"calc08-period\"]")));
        period.selectByVisibleText(String.valueOf(periodInMonths));
        elementMap.get("Interest").clear(); elementMap.get("Interest").sendKeys(String.valueOf(interestPercent));
        elementMap.get("Residual").clear(); elementMap.get("Residual").sendKeys(String.valueOf(residualPercent));
        elementMap.get("Deposit").sendKeys(String.valueOf(depositPercent));
        assertThat(driver.switchTo().alert().getText()).isEqualTo("Downpayment may not exceed the price of car!");
        driver.switchTo().alert().accept();
    }

    /**
     * This method enters invalid inputs to the fields and checks if validation appears
     */
    @Test
    public void fieldsValidationTest () {

        List<WebElement> middleBox = driver.findElements(By.className("middleBox"));
        boolean validationAppears;

        for(Map.Entry<String,WebElement> entry : elementMap.entrySet()){
            entry.getValue().sendKeys("Test");
           validationAppears = middleBox.stream().anyMatch(e->e.getAttribute("class").contains("error"));
           assertThat(validationAppears).isTrue();
           entry.getValue().clear(); entry.getValue().sendKeys("1");

        }
    }



    public void openAndMaximize() {
        driver.get("https://www.seb.ee/eng/loan-and-leasing/leasing/car-leasing#calculators");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/ul/li[1]/a")).click();
        driver.findElement(By.xpath("//*[@id=\"calculators-tab\"]/div/div[1]/div[1]/div/h3")).click();
        driver.switchTo().frame("calculator-frame-08a");
    }
}
