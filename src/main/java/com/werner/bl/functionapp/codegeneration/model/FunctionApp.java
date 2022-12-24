package com.werner.bl.functionapp.codegeneration.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FunctionApp {

    private String functionAppName;

    private List<FunctionAppTrigger> triggerList = new ArrayList<>();

    private List<FunctionAppClient> clientList = new ArrayList<>();

    public FunctionApp(String functionAppName) {
        this.functionAppName = functionAppName;
    }
}
