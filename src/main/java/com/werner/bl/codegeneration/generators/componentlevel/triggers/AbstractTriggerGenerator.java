package com.werner.bl.codegeneration.generators.componentlevel.triggers;

import com.werner.bl.codegeneration.generators.componentlevel.AbstractCodeComponentGenerator;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;

import java.util.Map;

public abstract class AbstractTriggerGenerator extends AbstractCodeComponentGenerator {

    protected abstract String generateTriggerCode(FunctionAppTrigger trigger);

    public String generateCodeString(FunctionAppTrigger trigger) {
        return generateTriggerCode(trigger);
    }
}