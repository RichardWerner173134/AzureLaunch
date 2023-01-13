package com.werner.bl.codegeneration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FunctionApp {

    public static String RESOURCE_GROUP_NAME;

    public static String APP_SERVICE_PLAN_NAME;

    @Getter
    private String functionAppName;

    @Getter
    private List<FunctionAppTrigger> triggerList = new ArrayList<>();

    @Getter
    private List<FunctionAppClient> clientList = new ArrayList<>();

    private int nextClientNumber = 1;

    private int nextTriggerNumber = 1;

    @Getter
    @Setter
    private int localPortNumber;

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

    public void addAppSettingToTrigger(FunctionAppTrigger trigger, String key, String value) {
        FunctionAppTrigger fat = triggerList.stream()
                .filter(c -> c.getTriggerName().equals(trigger.getTriggerName())).findFirst().get();

        fat.getTriggerParams().put(fat.getTriggerName() + key, value);
    }

    public void addAppSettingToClient(FunctionAppClient client, String key, String value) {
        FunctionAppClient fac = clientList.stream()
                .filter(c -> c.getClientName().equals(client.getClientName())).findFirst().get();

        fac.getClientParams().put(fac.getClientName() + key, value);
    }

}
