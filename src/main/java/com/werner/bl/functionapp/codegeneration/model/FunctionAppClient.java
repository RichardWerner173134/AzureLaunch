package com.werner.bl.functionapp.codegeneration.model;

import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppClientType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FunctionAppClient {
    private String clientName;

    private FunctionAppClientType clientType;
}
