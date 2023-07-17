package com.example.documents.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Payment {
    private Long id;
    private final String number;
    private final Date date;
    private final String user;
    private final BigDecimal amount;
    private final String employee;

    public Payment(Long id, String number, Date date, String user, BigDecimal amount, String employee) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.user = user;
        this.amount = amount;
        this.employee = employee;
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

    public String getEmployee() {
        return employee;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Платёжка от " + date + " номер " + number;
    }
}
