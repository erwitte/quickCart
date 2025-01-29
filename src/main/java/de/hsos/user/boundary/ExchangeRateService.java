package de.hsos.user.boundary;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExchangeRateService {
    static String apiUrl = "https://api.currencyapi.com/v3/latest?apikey=cur_live_JePNqDSP4i5KtBEFTDkF2mBTh9h2CZlwx73rfdoD&base_currency=EUR&currencies=";

    public static double getExchangeRate(String currency) {
        if (currency.equals("EUR")) {
            return 1.0;
        }
        return callCurrencyApi(currency);
    }

    private static double callCurrencyApi(String currency){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON Response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            double exchangeRate = rootNode.path("data").path(currency).path("value").asDouble();

            System.out.println("Exchange Rate (EUR to USD): " + exchangeRate);
            return exchangeRate;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
