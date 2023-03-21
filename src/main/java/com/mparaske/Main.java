package com.mparaske;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        CurrencyConverterGUI currencyConverterGUI = new CurrencyConverterGUI();
        currencyConverterGUI.setVisible(true);
        currencyConverterGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currencyConverterGUI.setSize(400, 200);
    }
}