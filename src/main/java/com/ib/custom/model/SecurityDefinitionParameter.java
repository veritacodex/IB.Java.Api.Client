package com.ib.custom.model;

import java.util.Set;

public class SecurityDefinitionParameter {
    public final int ReqId;
    public final String Exchange;
    public final int UnderlyingConId;
    public final String TradingClass;
    public final String Multiplier;
    public final Set<String> Expirations;
    public final Set<Double> Strikes;

    public SecurityDefinitionParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
        this.ReqId = reqId;
        this.Exchange = exchange;
        this.UnderlyingConId = underlyingConId;
        this.TradingClass = tradingClass;
        this.Multiplier = multiplier;
        this.Expirations = expirations;
        this.Strikes = strikes;
    }

    @Override
    public String toString() {
        return "SecurityDefinitionParameter{" +
                "ReqId=" + ReqId +
                ", Exchange='" + Exchange + '\'' +
                ", UnderlyingConId=" + UnderlyingConId +
                ", TradingClass='" + TradingClass + '\'' +
                ", Multiplier='" + Multiplier + '\'' +
                ", Expirations=" + Expirations.size() +
                ", Strikes=" + Strikes.size() +
                '}';
    }
}
