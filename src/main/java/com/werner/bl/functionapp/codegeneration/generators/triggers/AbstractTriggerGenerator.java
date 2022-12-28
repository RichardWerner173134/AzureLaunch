package com.werner.bl.functionapp.codegeneration.generators.triggers;

import com.werner.bl.functionapp.codegeneration.generators.AbstractCodeComponentGenerator;
import com.werner.bl.functionapp.codegeneration.generators.CodeGenerationResult;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;

import java.util.List;

public abstract class AbstractTriggerGenerator extends AbstractCodeComponentGenerator {

    protected abstract String generateTriggerCode(FunctionAppTrigger trigger);

    protected abstract List<String> getImportCode();

    public CodeGenerationResult generateCode(FunctionAppTrigger trigger) {
        String triggerCode = generateTriggerCode(trigger);
        List<String> importCode = getImportCode();

        return new CodeGenerationResult(triggerCode, importCode);
    }
}
