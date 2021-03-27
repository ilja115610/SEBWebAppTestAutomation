package maxLeasingCalculator;

public class MaxAmount implements MaxLeasingCalculator{



    private double netIncome;
    private double numOfDependants;
    private double familyMembersCoefficient = 1;
    private static final double standardLivingCost = 475;
    private double monthlyFinancialObligations;
    private static final double interest = 6;
    private static final double percent = 100;
    private static final double monthsPerYear = 12;
    private static final double leasePeriod = 7;




    public int getMaxAmount (double netIncome, double numOfDependants, double monthlyFinancialObligations) {

        this.netIncome = netIncome;
        this.numOfDependants = numOfDependants;
        this.monthlyFinancialObligations = monthlyFinancialObligations;

        double rate = (interest / percent) / monthsPerYear;
        double nper = leasePeriod * monthsPerYear;
        double pmt = getMaxLoanPaymentPerMonth();

        double x = Math.pow((1 + rate), -nper);
        double y = Math.pow((1 + rate), nper);

        double pv = ((x * (-pmt + y * pmt)) / rate);


        return (int)getRounded(pv,10.0);
    }


    private double getRounded (double amount, double level) {

        if (amount < 0 ) {
            return 0;
        }
        return Math.floor(amount / level) * level;
    }

    private double getMaxLoanPaymentPerMonth () {
        return Math.min(getMaxLoanSumByPercent(), getMaxLoanSumByLivingCost()) - getMonthlyFinancialObligationsAdjusted();
    }

    private double getMaxLoanSumByPercent () {
        return netIncome * getMaxLoanPercent();
    }

    private double getMaxLoanPercent () {
        if (netIncome < 700) return 0;
        if(netIncome < 1000) return 0.3;
        if(netIncome < 1300) return 0.35;
        if(netIncome < 1700) return 0.4;
        if(netIncome <= 2500) return 0.45;

        return 0.5;
    }

    private double getMaxLoanSumByLivingCost () {
        return netIncome - getLivingCost();
    }

    private double getLivingCost(){
        return getFamilyMembersApproximate() * standardLivingCost;
    }

    private double getFamilyMembersApproximate () {
        if (numOfDependants == 0) {
            return familyMembersCoefficient;
        } else if(numOfDependants == 1) {
            return familyMembersCoefficient + 0.4;
        }

        return familyMembersCoefficient + (numOfDependants - 1) * 0.3 + 0.4;
    }

    private double getMonthlyFinancialObligationsAdjusted () {
        if (monthlyFinancialObligations < 500) {
            return monthlyFinancialObligations * 1.15;
        } else if (monthlyFinancialObligations > 700) {
            return monthlyFinancialObligations * 1.25;
        }

        return monthlyFinancialObligations * 1.20;
    }


}
