package common;

public enum FormFields {

    PRICE ("priceOfVehicle") ,
    DEPOSIT("depositPercent"),
    PERIOD("periodInMonths"),
    INTEREST("interestPercent"),
    RESIDUAL("residualPercent"),
    INCOME("income"),
    DEPENDENTS("dependents"),
    LIABILITIES("liabilities");


    public final String value;

    FormFields(String value) {
        this.value = value;
    }
}
