package com.werner.bl.functionapp.codegeneration;

import com.werner.bl.functionapp.codegeneration.model.FunctionApp;
import org.springframework.stereotype.Component;

@Component
public class FunctionAppCodeGenerator {

    public FunctionAppCodeGenerationResult generateFunctionAppCode(FunctionApp edge) {
        // generate Ziparchive and store parameters for deployment

        return new FunctionAppCodeGenerationResult("", "", "");
    }
}
