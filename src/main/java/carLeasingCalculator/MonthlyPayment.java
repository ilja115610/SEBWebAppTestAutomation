package carLeasingCalculator;

import carLeasingCalculator.CarLeasingCalculator;

public class MonthlyPayment implements CarLeasingCalculator {


    private final static double kaebemaks = 0.20;



    @Override
    public double calculateMonthlyPayment(double hind, double smaks, double periood, double intress, double jaak) {

        intress = intress / 100;
        jaak = hind / (1 + kaebemaks) / 100 * jaak;
        smaks = hind*smaks/100;

        double neto = hind / (1 + kaebemaks) - (smaks / (1 + kaebemaks));
        double skoef = Math.round(neto * (1.0 + kaebemaks) * 100.0) / 100.0;
        double ikoef = intress * (1 / (11.8275 * (1 + kaebemaks)));

        double annuit = skoef * (ikoef * Math.pow(1 + ikoef, periood) / (Math.pow(1 + ikoef, periood) - 1));
        double kuuintr = neto * intress / 11.8275;

        return annuit - (jaak / neto * (annuit - kuuintr));
    }

}
