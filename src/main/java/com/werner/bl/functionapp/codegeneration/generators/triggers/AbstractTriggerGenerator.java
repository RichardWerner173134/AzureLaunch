package com.werner.bl.functionapp.codegeneration.generators.triggers;

import com.werner.bl.functionapp.codegeneration.generators.AbstractCodeComponentGenerator;
import com.werner.bl.functionapp.codegeneration.generators.CodeGenerationResult;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;

import java.util.List;

public abstract class AbstractTriggerGenerator extends AbstractCodeComponentGenerator {

    protected abstract String generateTriggerCode(FunctionAppTrigger trigger);

    protected abstract List<String> getImportCode();

    protected abstract List<String> getNecessaryDependencies();

    public CodeGenerationResult generateCode(FunctionAppTrigger trigger) {
        String triggerCode = generateTriggerCode(trigger);
        List<String> importCode = getImportCode();
        List<String> necessaryDependencies = getNecessaryDependencies();

        return new CodeGenerationResult(triggerCode, importCode, necessaryDependencies);
    }
}
