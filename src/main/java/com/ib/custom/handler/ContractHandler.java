package com.ib.custom.handler;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContractHandler {

    private final List<ContractDetails> contractDetailsList;
    private CompletableFuture<List<ContractDetails>> contractDetailsFuture;
    private int currentReqId;

    public ContractHandler() {
        this.contractDetailsList = new ArrayList<>();
    }

    public CompletableFuture<List<ContractDetails>> startRequest(int reqId, Contract contract) {
        this.currentReqId = reqId;
        this.contractDetailsFuture = new CompletableFuture<>();
        this.contractDetailsList.clear();
        return contractDetailsFuture;
    }

    public void handleContractDetails(int reqId, ContractDetails contractDetails) {
        if (reqId == currentReqId) {
            contractDetailsList.add(contractDetails);
        }
    }

    public void handleContractDetailsEnd(int reqId) {
        if (reqId == currentReqId && contractDetailsFuture != null) {
            contractDetailsFuture.complete(new ArrayList<>(contractDetailsList));
        }
    }
}
