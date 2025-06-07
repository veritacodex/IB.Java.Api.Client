package com.ib.custom.model;

import com.ib.client.*;
import com.ib.custom.handler.ContractHandler;
import com.ib.custom.handler.MarketDataHandler;
import com.ib.custom.handler.OptionParametersHandler;
import com.ib.custom.handler.PortfolioHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class IbClient implements EWrapper {

    //region [Definitions]
    private final EReaderSignal readerSignal;
    public final EClientSocket clientSocket;
    protected int currentOrderId = -1;
    private final PortfolioHandler portfolioHandler;
    private final ContractHandler contractHandler;
    private final MarketDataHandler marketDataHandler;
    private final OptionParametersHandler optionParametersHandler;

    public IbClient() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
        this.portfolioHandler = new PortfolioHandler();
        this.contractHandler = new ContractHandler();
        this.marketDataHandler = new MarketDataHandler();
        this.optionParametersHandler = new OptionParametersHandler();
    }

    public EClientSocket getClient() {
        return clientSocket;
    }

    public EReaderSignal getSignal() {
        return readerSignal;
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    @Override
    public void nextValidId(int orderId) {
        currentOrderId = orderId;
    }
    //endregion

    //region [Subscriptions]
    public void subscribeToAccountUpdates(Consumer<Account> subscriber) {
        this.portfolioHandler.accountSubscribers.add(subscriber);
    }

    public void subscribeToMarketDataUpdates(Consumer<MarketData> subscriber) {
        this.marketDataHandler.marketDataSubscribers.add(subscriber);
    }

    public void subscribeToOptionGreeksUpdates(Consumer<OptionGreeks> subscriber) {
        this.marketDataHandler.optionGreeksSubscribers.add(subscriber);
    }

    public void subscribeToRealTimeBarUpdates(Consumer<RealTimeBar> subscriber) {
        this.marketDataHandler.realTimeBarSubscribers.add(subscriber);
    }
    //endregion

    //region [Notifications]
    @Override
    public void error(Exception e) {
        System.out.println("Exception: " + e.getMessage());
    }

    @Override
    public void error(String str) {
        System.out.println("Error: " + str);
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        String str = "Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg;
        if (advancedOrderRejectJson != null) {
            str += (", AdvancedOrderRejectJson: " + advancedOrderRejectJson);
        }
        System.out.println(str);
    }
    //endregion

    //region [Account]
    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        this.portfolioHandler.updateAccountValue(key, value, currency, accountName);
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        this.portfolioHandler.notifyAccountUpdate();
    }
    //endregion

    //region [ContractDetails]
    public CompletableFuture<List<ContractDetails>> getContractDetails(int reqId, Contract contract) {
        System.out.println("Requesting contract details for:" +
                contract.symbol() + " Trading class:" + contract.tradingClass() + " Currency:" +
                contract.currency() + " Exchange:" + contract.exchange() + " SecType:" + contract.secType());
        CompletableFuture<List<ContractDetails>> future = startContractDetailsRequest(reqId, contract);
        clientSocket.reqContractDetails(reqId, contract);
        return future;
    }

    public CompletableFuture<List<ContractDetails>> startContractDetailsRequest(int reqId, Contract contract) {
        return contractHandler.startRequest(reqId, contract);
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        contractHandler.handleContractDetails(reqId, contractDetails);
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        contractHandler.handleContractDetailsEnd(reqId);
    }
    //endregion

    //region [MarketData]
    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
        this.marketDataHandler.notifyTickPriceUpdate(tickerId, field, price);
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        this.marketDataHandler.notifyRealTimeBarUpdate(reqId, time, open, high, low, close, volume, wap, count);
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        this.marketDataHandler.notifyOptionGreeks(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);
    }
    //endregion

    //region [OptionParameters]
    public CompletableFuture<List<SecurityDefinitionParameter>> getSecurityDefinitionParameters(int secDefOptParamsRequestId, String symbol, String secType, int conId) {
        CompletableFuture<List<SecurityDefinitionParameter>> future = startSecurityDefinitionParametersRequest();
        clientSocket.reqSecDefOptParams(secDefOptParamsRequestId, symbol, "", secType, conId);
        return future;
    }

    public CompletableFuture<List<SecurityDefinitionParameter>> startSecurityDefinitionParametersRequest() {
        return optionParametersHandler.startRequest();
    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
        SecurityDefinitionParameter parameter = new SecurityDefinitionParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes);
        this.optionParametersHandler.handleSecurityDefinitionParameter(parameter);
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        this.optionParametersHandler.handleSecurityDefinitionParameterEnd();
    }
    //endregion

    //region [Orders]
    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        if (order.whatIf()) {
            System.out.println("WhatIf Order:" + orderId);
        }
    }
    //endregion

    //region [Unused]
    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {

    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {

    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {

    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

    }

    @Override
    public void openOrderEnd() {

    }


    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {

    }

    @Override
    public void updateAccountTime(String timeStamp) {

    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {

    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {

    }

    @Override
    public void execDetailsEnd(int reqId) {

    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {

    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {

    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {

    }

    @Override
    public void managedAccounts(String accountsList) {

    }

    @Override
    public void receiveFA(int faDataType, String xml) {

    }

    @Override
    public void historicalData(int reqId, Bar bar) {

    }

    @Override
    public void scannerParameters(String xml) {

    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {

    }

    @Override
    public void scannerDataEnd(int reqId) {

    }

    @Override
    public void currentTime(long time) {

    }

    @Override
    public void fundamentalData(int reqId, String data) {

    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {

    }

    @Override
    public void tickSnapshotEnd(int reqId) {

    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {

    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {

    }

    @Override
    public void positionEnd() {

    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {

    }

    @Override
    public void accountSummaryEnd(int reqId) {

    }

    @Override
    public void verifyMessageAPI(String apiData) {

    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {

    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {

    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {

    }

    @Override
    public void displayGroupList(int reqId, String groups) {

    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectAck() {

    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {

    }

    @Override
    public void positionMultiEnd(int reqId) {

    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {

    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {

    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {

    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {

    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {

    }

    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {

    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {

    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {

    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Map.Entry<String, Character>> theMap) {

    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {

    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {

    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {

    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {

    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {

    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {

    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {

    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {

    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {

    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {

    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {

    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {

    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {

    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {

    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {

    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {

    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {

    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {

    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {

    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {

    }

    @Override
    public void completedOrdersEnd() {

    }

    @Override
    public void replaceFAEnd(int reqId, String text) {

    }

    @Override
    public void wshMetaData(int reqId, String dataJson) {

    }

    @Override
    public void wshEventData(int reqId, String dataJson) {

    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {

    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {

    }
    //endregion
}
