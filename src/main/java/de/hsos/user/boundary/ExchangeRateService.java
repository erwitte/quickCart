package de.hsos.user.boundary;

import de.hsos.user.boundary.acl.ExchangeRateAdapter;

public class ExchangeRateService {

    public static double getExchangeRate(String currency) {
        if (currency.equals("EUR")) {
            return 1.0;
        }
        return ExchangeRateAdapter.callCurrencyApi(currency);
    }
}
