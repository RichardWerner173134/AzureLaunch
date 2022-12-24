package com.werner.bl.functionapp.codegeneration.model;

import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppTriggerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FunctionAppTrigger {

    private String triggerName;

    private FunctionAppTriggerType triggerType;

}
