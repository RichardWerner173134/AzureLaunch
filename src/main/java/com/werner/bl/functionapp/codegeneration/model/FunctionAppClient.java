package com.werner.bl.functionapp.codegeneration.model;

import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppClientType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FunctionAppClient {

    @Setter
    private String clientName;

    private FunctionAppClientType clientType;

    private Map<String, String> clientParams = new HashMap<>();

    public FunctionAppClient(FunctionAppClientType clientType) {
        this.clientType = clientType;
    }
}
