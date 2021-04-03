package carLeasingCalculator;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import util.PropertyReader;
import webpages.CarLeasingPage;

import java.util.List;

import static common.FormFields.*;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarLeasingFunctionalCalculationsTest {

    private WebDriver driver;
    private PropertyReader propertyReader;
    private CarLeasingPage carLeasingPage;
    private CarLeasingCalculator carLeasingCalculator;
    private double priceOfVehicle;
    private double depositPercent;
    private double periodInMonths;
    private double interestPercent;
    private double residualPercent;


    @BeforeAll
    private void setup() {
        this.propertyReader = new PropertyReader();
        driver = getDriver();
        this.carLeasingPage = new CarLeasingPage(driver);
        this.carLeasingCalculator = new MonthlyPayment();
        openAndMaximize();
    }

    @BeforeEach
    public void clearBefore() {
        this.carLeasingPage.getCarLeasingFields().forEach(e->{
            if(e.getTagName().equals("select")){
                return;
            } e.clear();
        });
    }

    @AfterEach
    public void clearAfter() {
        this.carLeasingPage.getCarLeasingFields().forEach(e->{
            if(e.getTagName().equals("select")){
                return;
            } e.clear();
        });
    }

    @AfterAll
    public void cleanup() {
        driver.quit();
    }


    public WebDriver getDriver() {
        System.setProperty(propertyReader.getProperty("chromeDriver"),propertyReader.getProperty("path"));
        return new ChromeDriver();
    }

    /**
     * This method tests calculations with selected range of prices (10000-20000)
     * and other variables (downpayment, period, interest..) are fixed.
     */

    @Test
    public void priceRangeTest () {

        priceOfVehicle = 10000; depositPercent = 20; periodInMonths = 60; interestPercent = 3.5; residualPercent = 25;
        carLeasingPage.setInitialFormValues(priceOfVehicle,depositPercent,periodInMonths,interestPercent,residualPercent);
        double actualResult, expectedResult;
        for(int i = 10000; i <=20000; i+=500) {
            carLeasingPage.clearFormFields(carLeasingPage.getFieldByName(PRICE));
            carLeasingPage.setPriceOfVehicle(String.valueOf(i));
            actualResult = Double.parseDouble(carLeasingPage.getActualResult());
            expectedResult = calculateMonthlyPayment(i,depositPercent,periodInMonths,interestPercent,residualPercent);
            assertThat(actualResult).isEqualTo(expectedResult);

        }

    }

    /**
     * This method tests range of deposits from 10% - 70% with other variables fixed.
     */
    @Test
    public void depositRangeTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; residualPercent = 25; depositPercent = 10;
        carLeasingPage.setInitialFormValues(priceOfVehicle,depositPercent,periodInMonths,interestPercent,residualPercent);

        double actualResult, expectedResult;
        for(int i = 10; i <=70; i+=5) {
            carLeasingPage.clearFormFields(carLeasingPage.getFieldByName(DEPOSIT));
            carLeasingPage.setDepositPercent(String.valueOf(i));
            actualResult = Double.parseDouble(carLeasingPage.getActualResult());
            expectedResult = calculateMonthlyPayment(priceOfVehicle,i,periodInMonths,interestPercent,residualPercent);
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }

    /**
     * This method tests range of residual values 10%-70% with other variables fixed.
     */
    @Test
    public void residualRangeTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; depositPercent = 20; residualPercent = 25;
        carLeasingPage.setInitialFormValues(priceOfVehicle,depositPercent,periodInMonths,interestPercent,residualPercent);

        double actualResult, expectedResult;
        for(int i = 10; i <=70; i+=5) {
            carLeasingPage.clearFormFields(carLeasingPage.getFieldByName(RESIDUAL));
            carLeasingPage.setResidualPercent(String.valueOf(i));
            actualResult = Double.parseDouble(carLeasingPage.getActualResult());
            expectedResult = calculateMonthlyPayment(priceOfVehicle,depositPercent,periodInMonths,interestPercent,i);
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }

    /**
     * Negative testing of deposit amount
     */
    @Test
    public void depositMoreThanPriceTest () {

        priceOfVehicle = 5000; periodInMonths = 60; interestPercent = 3.5; depositPercent = 20; residualPercent = 25;
        carLeasingPage.setInitialFormValues(priceOfVehicle,depositPercent,periodInMonths,interestPercent,residualPercent);
        carLeasingPage.clearFormFields(carLeasingPage.getFieldByName(DEPOSIT));
        carLeasingPage.setDepositPercent(String.valueOf(101));
        assertThat(driver.switchTo().alert().getText()).isEqualTo("Downpayment may not exceed the price of car!");
        driver.switchTo().alert().accept();
    }

    /**
     * This method enters invalid inputs to the fields and checks if validation appears
     */
    @Test
    public void fieldsValidationTest () {

        List<WebElement> middleBox = driver.findElements(By.className("middleBox"));
            carLeasingPage.getCarLeasingFields().forEach(e->{
                e.sendKeys("Test");
                assertThat(middleBox.stream().anyMatch(el -> el.getAttribute("class").contains("error"))).isTrue();
            });
    }

    public double calculateMonthlyPayment (double price, double deposit, double period, double interest, double residual) {
        return this.carLeasingCalculator.calculateMonthlyPayment(price,deposit, period, interest,residual);
    }


    public void openAndMaximize() {

        driver.get("https://www.seb.ee/eng/loan-and-leasing/leasing/car-leasing#calculators");
        driver.manage().window().maximize();
        carLeasingPage.acceptCookies();
        carLeasingPage.expandCarLeasingCalculatorSection();
        driver.switchTo().frame("calculator-frame-08a");
    }
}
