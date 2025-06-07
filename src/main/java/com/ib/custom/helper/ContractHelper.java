package com.ib.custom.helper;

import com.ib.client.Contract;
import com.ib.client.Types;
import com.ib.custom.model.SecurityDefinitionParameter;

public class ContractHelper {

    public static Contract getSpxContract() {
        Contract contract = new Contract();
        contract.symbol("SPX");
        contract.secType(Types.SecType.IND);
        contract.currency("USD");
        return contract;
    }

    public static Contract getEsContract() {
        Contract contract = new Contract();
        contract.symbol("ES");
        contract.secType(Types.SecType.CONTFUT);
        contract.exchange("CME");
        contract.currency("USD");
        return contract;
    }

    public static Contract getLegRequestContract(String expiration, double strike, Types.Right right, SecurityDefinitionParameter securityDefinitionParameter) {
        Contract contract = new Contract();
        contract.symbol(securityDefinitionParameter.TradingClass);
        contract.lastTradeDateOrContractMonth(expiration);
        contract.strike(strike);
        contract.right(right);
        contract.multiplier(securityDefinitionParameter.Multiplier);
        contract.exchange(securityDefinitionParameter.Exchange);
        contract.tradingClass(securityDefinitionParameter.TradingClass);
        contract.secType(Types.SecType.OPT);
        contract.currency("USD");
        return contract;
    }
}
