package com.ib.custom.handler;

import com.ib.client.Decimal;
import com.ib.custom.model.MarketData;
import com.ib.custom.model.OptionGreeks;
import com.ib.custom.model.RealTimeBar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MarketDataHandler {
    public final List<Consumer<MarketData>> marketDataSubscribers = new ArrayList<>();
    public final List<Consumer<RealTimeBar>> realTimeBarSubscribers = new ArrayList<>();
    public final List<Consumer<OptionGreeks>> optionGreeksSubscribers = new ArrayList<>();

    public void notifyTickPriceUpdate(int tickerId, int field, double price) {
        MarketData marketData = new MarketData(tickerId, field, price);
        marketDataSubscribers.forEach(subscriber -> subscriber.accept(marketData));
    }

    public void notifyRealTimeBarUpdate(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        RealTimeBar realTimeBar = new RealTimeBar(reqId, time, open, high, low, close, volume, wap, count);
        realTimeBarSubscribers.forEach(subscriber -> subscriber.accept(realTimeBar));
    }

    public void notifyOptionGreeks(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        OptionGreeks optionGreeks = new OptionGreeks(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);
        optionGreeksSubscribers.forEach(subscriber -> subscriber.accept(optionGreeks));
    }
}
