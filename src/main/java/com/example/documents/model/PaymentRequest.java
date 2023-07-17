package com.example.documents.model;

import java.math.BigDecimal;
import java.sql.Date;

public class PaymentRequest {
    private Long id;
    private final String number;
    private final Date date;
    private final String user;
    private final String counterpart;
    private final BigDecimal amount;
    private final String currency;
    private final BigDecimal currencyRate;
    private final BigDecimal commission;

    public PaymentRequest(Long id,
                          String number,
                          Date date,
                          String user,
                          String counterpart,
                          BigDecimal amount,
                          String currency,
                          BigDecimal currencyRate,
                          BigDecimal commission) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.user = user;
        this.counterpart = counterpart;
        this.amount = amount;
        this.currency = currency;
        this.currencyRate = currencyRate;
        this.commission = commission;
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

    public String getCounterpart() {
        return counterpart;
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

    public BigDecimal getCommission() {
        return commission;
    }

    @Override
    public String toString() {
        return "Заявка на оплату от " + date + " номер " + number;
    }
}
