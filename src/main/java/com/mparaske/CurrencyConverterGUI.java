package com.mparaske;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CurrencyConverterGUI extends JFrame {

    private final CurrencyConverter currencyConverter;

    public CurrencyConverterGUI() {
        currencyConverter = new CurrencyConverter();
        initComponents();
    }

    private void initComponents() {
        setTitle("Currency Converter");

        // Create and set layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create labels and text fields
        JLabel fromCurrencyLabel = new JLabel("From Currency:");
        JComboBox<CurrencyItem> fromCurrencyBox = new JComboBox<>(currencyConverter.getAvailableCurrencies().toArray(new CurrencyItem[0]));
        JLabel toCurrencyLabel = new JLabel("To Currency:");
        JComboBox<CurrencyItem> toCurrencyBox = new JComboBox<>(currencyConverter.getAvailableCurrencies().toArray(new CurrencyItem[0]));
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField(10);
        JLabel resultLabel = new JLabel("Result:");
        JTextField resultField = new JTextField(10);
        resultField.setEditable(false);

        // Create convert button and add action listener
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(e -> {
            try {
                String fromCurrency = ((CurrencyItem) Objects.requireNonNull(fromCurrencyBox.getSelectedItem())).getCurrencyCode();
                String toCurrency = ((CurrencyItem) Objects.requireNonNull(toCurrencyBox.getSelectedItem())).getCurrencyCode();
                double amount = Double.parseDouble(amountField.getText().trim());
                double result = currencyConverter.convertCurrency(fromCurrency, toCurrency, amount);
                resultField.setText(String.format("%.2f", result));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid currency codes and amount.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Conversion failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to layout
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 0;
        add(fromCurrencyLabel, c);
        c.gridx = 1;
        add(fromCurrencyBox, c);
        c.gridx = 0;
        c.gridy = 1;
        add(toCurrencyLabel, c);
        c.gridx = 1;
        add(toCurrencyBox, c);
        c.gridx = 0;
        c.gridy = 2;
        add(amountLabel, c);
        c.gridx = 1;
        add(amountField, c);
        c.gridx = 0;
        c.gridy = 3;
        add(resultLabel, c);
        c.gridx = 1;
        add(resultField, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(convertButton, c);
    }


}
