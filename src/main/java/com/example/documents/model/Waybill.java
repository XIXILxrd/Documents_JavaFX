package com.example.documents.model;

import java.math.BigDecimal;
import java.sql.Date;


public class Waybill {
    private Long id;
    private final String number;
    private final Date date;
    private final String user;
    private final BigDecimal amount;
    private final String currency;
    private final BigDecimal currencyRate;
    private final String item;
    private final double quantity;

    public Waybill(Long id,
                   String number,
                   Date date,
                   String user,
                   BigDecimal amount,
                   String currency,
                   BigDecimal currencyRate,
                   String item,
                   double quantity) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.user = user;
        this.amount = amount;
        this.currency = currency;
        this.currencyRate = currencyRate;
        this.item = item;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public String getItem() {
        return item;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Накладная от " + date + " номер " + number;
    }
}
