package com.ib.custom.model;

public class OptionGreeks {

    public final int RequestId;
    public final int Field;
    public final int TickAttrib;
    public final double ImpliedVol;
    public final double Delta;
    public final double OptPrice;
    public final double PvDividend;
    public final double Gamma;
    public final double Vega;
    public final double Theta;
    public final double UndPrice;

    public OptionGreeks(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        this.RequestId = tickerId;
        this.Field = field;
        this.TickAttrib = tickAttrib;
        this.ImpliedVol = impliedVol;
        this.Delta = delta;
        this.OptPrice = optPrice;
        this.PvDividend = pvDividend;
        this.Gamma = gamma;
        this.Vega = vega;
        this.Theta = theta;
        this.UndPrice = undPrice;
    }
}
