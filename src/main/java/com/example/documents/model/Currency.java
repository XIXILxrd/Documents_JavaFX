package com.example.documents.model;

public enum Currency {
    RUB("Рубль"),
    EUR("Евро"),
    BYN("Беларусский рубль"),
    USD("Доллар США"),
    KZT("Тенге");
    private final String currencyName;

    Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return currencyName;
    }
}
