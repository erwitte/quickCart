package de.hsos.user.boundary.acl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateAdapter {
    static String apiUrl = "https://api.currencyapi.com/v3/latest?apikey=cur_live_JePNqDSP4i5KtBEFTDkF2mBTh9h2CZlwx73rfdoD&base_currency=EUR&currencies=";

    public static double callCurrencyApi(String currency){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            return rootNode.path("data").path(currency).path("value").asDouble();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0.0;
        }
    }
}
