package com.werner.bl.functionapp.codegeneration.model;

import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppClientType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FunctionAppClient {
    private String clientName;

    private FunctionAppClientType clientType;

    private Map<String, String> clientParams = new HashMap<>();

    public FunctionAppClient(String clientName, FunctionAppClientType clientType) {
        this.clientName = clientName;
        this.clientType = clientType;
    }
}
