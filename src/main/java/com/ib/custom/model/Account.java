package com.ib.custom.model;

import java.util.Objects;

public class Account {

    public String AccountCode;
    public String AccountType;
    public double AvailableFunds;
    public double BuyingPower;
    public double CashEuro;
    public double CashUsd;

    public void updateAccountValue(String key, String value, String currency, String accountName) {
        AccountCode = Objects.equals(key, "AccountCode") ? value : AccountCode;
        AccountType = Objects.equals(key, "AccountType") ? value : AccountCode;
        AvailableFunds = Objects.equals(key, "AvailableFunds") ? Double.parseDouble(value) : AvailableFunds;
        BuyingPower = Objects.equals(key, "BuyingPower") ? Double.parseDouble(value) : BuyingPower;
        CashEuro = Objects.equals(key, "CashBalance") && Objects.equals(currency, "EUR") ? Double.parseDouble(value) : CashEuro;
        CashUsd = Objects.equals(key, "CashBalance") && Objects.equals(currency, "USD") ? Double.parseDouble(value) : CashUsd;
    }
}
