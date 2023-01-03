package com.werner.bl.codegeneration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionApp {

    public static String RESOURCE_GROUP_NAME;

    public static String APP_SERVICE_PLAN_NAME;

    @Getter
    private String functionAppName;

    @Getter
    private List<FunctionAppTrigger> triggerList = new ArrayList<>();

    @Getter
    private List<FunctionAppClient> clientList = new ArrayList<>();

    // TODO encapsulation, provide a function findById, because you shouldnt add keyvalue pairs without using addConnectionString(...)
    @Getter
    @Setter
    private Map<String, String> additionalProperties = new HashMap<>();

    private int nextClientNumber = 1;

    private int nextTriggerNumber = 1;

    private int nextConnectionStringNumber = 1;

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

    public void addConnectionString(String triggerName, String connectionString) {
        String valueName = triggerName + nextConnectionStringNumber;
        additionalProperties.put(valueName, connectionString);
    }
}
