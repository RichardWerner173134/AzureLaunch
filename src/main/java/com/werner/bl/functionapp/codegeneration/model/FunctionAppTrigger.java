package com.werner.bl.functionapp.codegeneration.model;

import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppTriggerType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FunctionAppTrigger {

    private String triggerName;

    private FunctionAppTriggerType triggerType;

    private Map<String, String> triggerParams = new HashMap<>();


    public FunctionAppTrigger(String triggerName, FunctionAppTriggerType triggerType) {
        this.triggerName = triggerName;
        this.triggerType = triggerType;
    }
}
