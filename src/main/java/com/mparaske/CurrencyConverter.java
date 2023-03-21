package com.mparaske;

import okhttp3.*;
import com.google.gson.*;

import java.util.Map;
import java.util.Objects;

public class CurrencyConverter {

    private static final String API_URL = "https://openexchangerates.org/api/latest.json";
    private static final String API_KEY = "614a4a96ef1b428db76f923a81924516";
    private static final Gson GSON = new Gson();
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private Map<String, Double> conversionRates;

    public CurrencyConverter() {
        fetchConversionRates();
    }

    private void fetchConversionRates() {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(API_URL)).newBuilder();
        urlBuilder.addQueryParameter("app_id", API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String json = response.body().string();
                JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);
                JsonObject ratesObject = jsonObject.getAsJsonObject("rates");
                conversionRates = GSON.fromJson(ratesObject.toString(), Map.class);
            } else {
                throw new RuntimeException("Failed to retrieve conversion rates");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        double fromRate = conversionRates.get(fromCurrency);
        double toRate = conversionRates.get(toCurrency);
        return amount / fromRate * toRate;
    }
}
