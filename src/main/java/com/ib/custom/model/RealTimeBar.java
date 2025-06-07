package com.ib.custom.model;

import com.ib.client.Decimal;

public class RealTimeBar {

    public final int ReqId;
    public final long Time;
    public final double Open;
    public final double High;
    public final double Low;
    public final double Close;
    public final Decimal Volume;
    public final Decimal Wap;
    public final int Count;

    public RealTimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        this.ReqId = reqId;
        this.Time = time;
        this.Open = open;
        this.High = high;
        this.Low = low;
        this.Close = close;
        this.Volume = volume;
        this.Wap = wap;
        this.Count = count;
    }
}
