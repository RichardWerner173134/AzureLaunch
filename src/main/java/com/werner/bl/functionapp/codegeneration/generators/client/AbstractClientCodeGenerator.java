package com.werner.bl.functionapp.codegeneration.generators.client;

import com.werner.bl.functionapp.codegeneration.generators.AbstractCodeComponentGenerator;
import com.werner.bl.functionapp.codegeneration.generators.CodeGenerationResult;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppClient;

import java.util.List;

public abstract class AbstractClientCodeGenerator extends AbstractCodeComponentGenerator {

    protected abstract String generateClientCode(FunctionAppClient client);

    protected abstract List<String> getImportCode();

    protected abstract List<String> getNecessaryDependencies();

    public CodeGenerationResult generateCode(FunctionAppClient client) {
        String code = generateClientCode(client);
        List<String> necessaryImports = getImportCode();
        List<String> necessaryDependencies = getNecessaryDependencies();


        return new CodeGenerationResult(code, necessaryImports, necessaryDependencies);
    }

}
