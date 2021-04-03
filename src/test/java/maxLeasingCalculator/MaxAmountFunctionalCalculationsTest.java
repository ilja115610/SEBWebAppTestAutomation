package maxLeasingCalculator;

import static formFields.FormFields.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import webpages.MaxLeasePage;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MaxAmountFunctionalCalculationsTest {

    private WebDriver driver;
    private static final String path = "src/main/resources/chromedriver/chromedriver.exe";
    private static final String chromeDriver = "webdriver.chrome.driver";
    private MaxLeasingCalculator calculator;
    private MaxLeasePage maxLeasePage;
    private int income;
    private int liabilities;
    private int dependents;


    @BeforeAll
    private void setup() {
        driver = getDriver();
        calculator = new MaxAmount();
        maxLeasePage = new MaxLeasePage(driver);
        openAndMaximize();

    }

    @AfterAll
    public void cleanup() {
        driver.quit();
    }

    @AfterEach
    public void clearAfter () {
       this.maxLeasePage.getMaxLeaseFields().forEach(WebElement::clear);
    }

    @BeforeEach
    public void clearBefore () {
        this.maxLeasePage.getMaxLeaseFields().forEach(WebElement::clear);
    }

    public WebDriver getDriver() {
        System.setProperty(chromeDriver, path);
        return new ChromeDriver();
    }

    /**
     * This method tests calculation with range of income values 1200 - 3000
     * and other variables fixed
     */
    @Test
    public void incomeRangeTest () {

        dependents = 0; liabilities = 0; income = 0;
        maxLeasePage.setInitialFormValues(income,dependents,liabilities);

        int expectedResult, actualResult;
        for(int i = 1200; i<= 3000; i+=200){
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(INCOME));
            maxLeasePage.setIncomeValue(String.valueOf(i));
            actualResult = Integer.parseInt(maxLeasePage.getActualResult());
            expectedResult = calculator.getMaxAmount(i,dependents,liabilities);
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }

    /**
     * This method tests calculation with range of liabilities values 10 - 300
     * and other variables fixed
     */

    @Test
    public void liabilitiesRangeTest () {

        dependents = 0; liabilities = 0; income = 1200;
        maxLeasePage.setInitialFormValues(income,dependents,liabilities);

        int expectedResult, actualResult;
        for (int i = 10; i <= 300; i += 20) {
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(LIABILITIES));
            maxLeasePage.setLiabilitiesValue(String.valueOf(i));
            actualResult = Integer.parseInt(maxLeasePage.getActualResult());
            expectedResult = calculator.getMaxAmount(income,dependents,i);
            assertThat(actualResult).isEqualTo(expectedResult);
        }
    }

    /**
     * This method tests calculation with range of dependents values 0 - 4
     * and other variables fixed
     */
    @Test
    public void dependentsRangeTest () {

        dependents = 0; liabilities = 0; income = 1200;
        maxLeasePage.setInitialFormValues(income,dependents,liabilities);

        int expectedResult, actualResult;
        for (int i = 0; i <= 4; i ++) {
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(DEPENDENTS));
            maxLeasePage.setDependentsValue(String.valueOf(i));
            actualResult = Integer.parseInt(maxLeasePage.getActualResult());
            expectedResult = calculator.getMaxAmount(income,i,liabilities);
            assertThat(actualResult).isEqualTo(expectedResult);
        }

    }


    /**
     * This method test calculations when number of dependents exceed financial capabilities for car lease.
     */
    @Test
    public void dependentsRangeExceedTest () {

        dependents = 0; liabilities = 0; income = 1200;
        maxLeasePage.setInitialFormValues(income,dependents,liabilities);

        for (int i = 5; i <= 8; i ++) {
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(DEPENDENTS));
            maxLeasePage.setDependentsValue(String.valueOf(i));
            assertThat(maxLeasePage.getResultMsgUpper()).isEqualTo("We cannot provide financing with the entered data. Add a surety, if possible.");
        }
    }

    /**
     * This method tests calculations when liabilities amount exceeds financial capabilities for lease.
     */
    @Test
    public void liabilitiesRangeExceedTest () throws InterruptedException {

        dependents = 0; liabilities = 0; income = 1200;
        maxLeasePage.setInitialFormValues(income,dependents,liabilities);

        int expectedResult, actualResult;
        for (int i = 200; i <= 350; i +=20) {
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(LIABILITIES));
            maxLeasePage.setLiabilitiesValue(String.valueOf(i));
            if(i<302){
                actualResult = Integer.parseInt(maxLeasePage.getActualResult());
                expectedResult = calculator.getMaxAmount(income, dependents, i);
                assertThat(actualResult).isEqualTo(expectedResult);
                continue;
            }
            assertThat(maxLeasePage.getResultMsgLower()).isEqualTo("With the entered data, the maximum lease amount is below 5,000 euros. If possible, add a surety or review SEB car loan.");
            maxLeasePage.clearFormFields(maxLeasePage.getFieldByName(LIABILITIES));
        }
    }


    public void openAndMaximize() {
        driver.get("https://www.seb.ee/eng/loan-and-leasing/leasing/car-leasing#calculators");
        driver.manage().window().maximize();
        maxLeasePage.expandMaxLeaseCalculatorSection();


    }

}
