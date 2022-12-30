package com.werner.bl.codegeneration.model;

import com.werner.bl.codegeneration.model.enums.FunctionAppTriggerType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FunctionAppTrigger {

    @Setter
    private String triggerName;

    private FunctionAppTriggerType triggerType;

    private Map<String, String> triggerParams = new HashMap<>();


    public FunctionAppTrigger(FunctionAppTriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
