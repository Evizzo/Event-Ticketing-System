package com.eventticketingsystem.eventticketingsystem.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CurrencyConverterService {
    @Value("${currency-converter.api-key}")
    private String API_KEY;
    public BigDecimal convertEventPriceCurrency(String toCurrency, BigDecimal amount) {
        String fromCurrency = "USD";
        String url_str = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + fromCurrency;

        toCurrency = toCurrency.toUpperCase();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url_str))
                .build();

        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader(response.body())).getAsJsonObject();
            JsonObject conversionRates = root.getAsJsonObject("conversion_rates");

            if (conversionRates.has(toCurrency)) {
                return amount.multiply(conversionRates.get(toCurrency).getAsBigDecimal());
            } else {
                throw new IllegalArgumentException("Invalid currency code: " + toCurrency);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}