package com.ib.custom.handler;

import com.ib.custom.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PortfolioHandler {

    private final Account account = new Account();
    public final List<Consumer<Account>> accountSubscribers = new ArrayList<>();

    public void updateAccountValue(String key, String value, String currency, String accountName) {
        account.updateAccountValue(key, value, currency, accountName);
    }

    public void notifyAccountUpdate() {
        accountSubscribers.forEach(subscriber -> subscriber.accept(account));
    }
}
