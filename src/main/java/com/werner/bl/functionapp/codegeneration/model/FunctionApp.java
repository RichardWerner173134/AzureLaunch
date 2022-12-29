package com.werner.bl.functionapp.codegeneration.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FunctionApp {

    @Getter
    private String functionAppName;

    @Getter
    private List<FunctionAppTrigger> triggerList = new ArrayList<>();

    @Getter
    private List<FunctionAppClient> clientList = new ArrayList<>();

    private int nextClientNumber = 1;

    private int nextTriggerNumber = 1;

    public FunctionApp(String functionAppName) {
        this.functionAppName = functionAppName;
    }

    public void addClient(FunctionAppClient client) {
        String name = "client" + nextClientNumber;
        client.setClientName(name);
        nextClientNumber += 1;
        clientList.add(client);
    }

    public void addTrigger(FunctionAppTrigger trigger) {
        String name = "trigger" + nextTriggerNumber;
        trigger.setTriggerName(name);
        nextTriggerNumber += 1;
        triggerList.add(trigger);
    }
}
