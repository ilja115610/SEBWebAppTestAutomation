package webpages;

import common.FormFields;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MaxLeasePage {


    private WebDriver driver;

    private JavascriptExecutor executor;

    @FindBy(xpath = "//*[@id=\"calculators-tab\"]/div/div[1]/div[1]/div/h3")
    private WebElement maximumLeasingSectionLink;

    @FindBy(xpath = "//*[@id=\"netoIncome\"]")
    private WebElement income;

    @FindBy(xpath = "//*[@id=\"numOfDependants\"]")
    private WebElement dependents;

    @FindBy(xpath = "//*[@id=\"monthlyFinancialObligations\"]")
    private WebElement liabilities;

    @FindBy(xpath = "/html/body/div[5]/div/div[4]/ul/li[1]/a")
    private WebElement maxLeaseSectionLink;

    @FindBy(xpath = "//*[@id=\"leaseSum\"]")
    private WebElement maxAmount;

    @FindBy(xpath = "//*[@id=\"resultWrapperTextFive\"]")
    private WebElement resultMsgLower;

    @FindBy(xpath = "//*[@id=\"resultWrapperTextThree\"]")
    private WebElement resultMsgUpper;

    public MaxLeasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.executor = (JavascriptExecutor) this.driver;

    }

    public void setIncomeValue(String value) {
        this.income.sendKeys(value);
    }

    public void setDependentsValue(String value) {
        this.dependents.sendKeys(value);
    }

    public void setLiabilitiesValue(String value) {
        this.liabilities.sendKeys(value);
    }

    public void setInitialFormValues(Integer... fields) {

        for(int i = 0; i < fields.length; i++) {
            getMaxLeaseFields().get(i).sendKeys(String.valueOf(fields[i]));
        }
    }

    public void clearFormFields(WebElement... elements) {
        List.of(elements)
                .forEach(WebElement::clear);
    }


    public String getActualResult() {
        this.executor.executeScript("arguments[0].removeAttribute('style')", maxAmount);
        return this.maxAmount.getText().replaceAll(" ","");
    }

    public String getResultMsgLower () {
        this.executor.executeScript("arguments[0].removeAttribute('style')", resultMsgLower);
        return this.resultMsgLower.getText();
    }

    public String getResultMsgUpper () {
        this.executor.executeScript("arguments[0].removeAttribute('style')", resultMsgUpper);
        return this.resultMsgUpper.getText();
    }

    public List<WebElement> getMaxLeaseFields(){
        return  List.of(income,dependents,liabilities);
    }

    public void expandMaxLeaseCalculatorSection() {
        this.maxLeaseSectionLink.click();
    }

    public WebElement getFieldByName (FormFields field) {
        switch (field) {
            case INCOME:
                return this.income;
            case DEPENDENTS:
                return this.dependents;
            case LIABILITIES:
                return this.liabilities;
            default: throw new NoSuchElementException("No such element found");
        }

    }

}
