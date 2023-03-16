package com.mparaske;


public class Main {
    public static void main(String[] args) {
        CurrencyConverter currencyConverter = new CurrencyConverter();
        double convertedAmount = currencyConverter.convertCurrency("USD", "EUR", 100);
        System.out.println(convertedAmount);
    }
}