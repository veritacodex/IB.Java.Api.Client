package com.ib.custom.model;

public class MarketData {
    public int Id;
    public int Field;
    public double Price;

    public MarketData(int tickerId, int field, double price) {
        this.Id = tickerId;
        this.Field = field;
        this.Price = price;
    }
}
