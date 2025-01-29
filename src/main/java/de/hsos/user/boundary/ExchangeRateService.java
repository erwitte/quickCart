package de.hsos.user.boundary;

public class ExchangeRateService {
    public static double getExchangeRate(String currency) {
        if (currency.equals("EUR")) {
            return 1.0;
        }
        return 2;
    }
}
