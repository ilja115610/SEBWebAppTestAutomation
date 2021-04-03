package webpages;

import common.FormFields;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import java.util.List;


public class CarLeasingPage {

    private WebDriver driver;

    private JavascriptExecutor executor;

    @FindBy(xpath = "//*[@id=\"calculators-tab\"]/div/div[1]/div[1]/div/h3")
    private WebElement carLeasingSectionLink;

    @FindBy(xpath = "//*[@id=\"calc08-sum\"]")
    private WebElement priceOfVehicle;

    @FindBy(xpath = "//*[@id=\"calc08-deposit\"]")
    private WebElement depositPercent;

    @FindBy(xpath = "//*[@id=\"calc08-int\"]")
    private WebElement interestPercent;

    @FindBy(xpath = "//*[@id=\"calc08-salvage-value\"]")
    private WebElement residualPercent;

    @FindBy(xpath = "//*[@id=\"monthly-result\"]/span/i")
    private WebElement monthlyResult;

    @FindBy(xpath = "//*[@id=\"calc08-period\"]")
    private WebElement periodInMonths;

    @FindBy(xpath = "/html/body/div[5]/div/div[4]/ul/li[1]/a")
    private WebElement acceptCookiesButton;



    public CarLeasingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.executor = (JavascriptExecutor) this.driver;

    }

    public void clearFormFields(WebElement... elements) {
        List.of(elements)
                .forEach(WebElement::clear);
    }

    public void acceptCookies() {
        this.acceptCookiesButton.click();
    }

    public void setPriceOfVehicle (String price) {

        this.priceOfVehicle.sendKeys(price);
    }

    public void setDepositPercent(String depositPercent) {

        this.depositPercent.sendKeys(depositPercent);
    }

    public void setInterestPercent(String interestPercent) {

        this.interestPercent.sendKeys(interestPercent);
    }

    public void setResidualPercent(String residualPercent) {

        this.residualPercent.sendKeys(residualPercent);
    }

    public void setInitialFormValues(Double... fields) {

        for(int i = 0; i < fields.length; i++) {
            getCarLeasingFields().get(i).sendKeys(String.valueOf(fields[i]));
        }
    }

    public void expandCarLeasingCalculatorSection() {
        this.carLeasingSectionLink.click();
    }

    public String getActualResult() {
        this.executor.executeScript("arguments[0].removeAttribute('style')", monthlyResult);
       return this.monthlyResult.getText();
    }

    public void setPeriodInMonths(String period) {
        Select periodMonths = new Select(periodInMonths);
        periodMonths.selectByVisibleText(period);
    }

    public List<WebElement> getCarLeasingFields(){
        return  List.of(priceOfVehicle, depositPercent, periodInMonths, interestPercent, residualPercent);
    }

    public WebElement getFieldByName (FormFields field) {
        switch (field) {
            case PRICE:
                return this.priceOfVehicle;
            case DEPOSIT:
                return this.depositPercent;
            case PERIOD:
                return this.periodInMonths;
            case INTEREST:
                return this.interestPercent;
            case RESIDUAL:
                return this.residualPercent;
            default: throw new NoSuchElementException("No such element found");
        }

    }


}
