package com.mparaske;

import okhttp3.*;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CurrencyConverter {

    private static final String API_URL = "https://openexchangerates.org/api/latest.json";
    private static final String API_KEY = "614a4a96ef1b428db76f923a81924516";
    private static final Gson GSON = new Gson();
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private Map<String, Double> conversionRates;
    private Map<String, String> currencyDisplayNames;

    public CurrencyConverter() {
        fetchConversionRates();
        loadCurrencyDisplayNames();
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

    public List<CurrencyItem> getAvailableCurrencies() {
        return conversionRates.keySet().stream()
                .map(code -> new CurrencyItem(code, getCurrencyDisplayName(code)))
                .sorted(Comparator.comparing(CurrencyItem::getCurrencyName))
                .collect(Collectors.toList());
    }

    private void loadCurrencyDisplayNames() {
        currencyDisplayNames = new HashMap<>();

        try (InputStream inputStream = getClass().getResourceAsStream("/currency_display_names.txt");
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    currencyDisplayNames.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrencyDisplayName(String code) {
        return currencyDisplayNames.getOrDefault(code, code);
    }
}
