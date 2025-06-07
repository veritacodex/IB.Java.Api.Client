package com.ib.custom.handler;

import com.ib.custom.model.SecurityDefinitionParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OptionParametersHandler {

    private final List<SecurityDefinitionParameter> securityDefinitionParameterList;
    private CompletableFuture<List<SecurityDefinitionParameter>> securityDefinitionParameterFuture;

    public OptionParametersHandler() {
        this.securityDefinitionParameterList = new ArrayList<>();
    }

    public CompletableFuture<List<SecurityDefinitionParameter>> startRequest() {
        this.securityDefinitionParameterFuture = new CompletableFuture<>();
        this.securityDefinitionParameterList.clear();
        return securityDefinitionParameterFuture;
    }

    public void handleSecurityDefinitionParameter(SecurityDefinitionParameter securityDefinitionParameter) {
        securityDefinitionParameterList.add(securityDefinitionParameter);
    }

    public void handleSecurityDefinitionParameterEnd() {
        if (securityDefinitionParameterFuture != null) {
            securityDefinitionParameterFuture.complete(new ArrayList<>(securityDefinitionParameterList));
        }
    }
}
