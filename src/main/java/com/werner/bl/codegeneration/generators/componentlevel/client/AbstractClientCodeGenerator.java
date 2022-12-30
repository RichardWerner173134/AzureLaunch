package com.werner.bl.codegeneration.generators.componentlevel.client;

import com.werner.bl.codegeneration.generators.componentlevel.AbstractCodeComponentGenerator;
import com.werner.bl.codegeneration.model.FunctionAppClient;

public abstract class AbstractClientCodeGenerator extends AbstractCodeComponentGenerator {

    protected abstract String generateClientCode(FunctionAppClient client);

    public String generateCodeString(FunctionAppClient client) {
        return generateClientCode(client);
    }
}
