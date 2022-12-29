package com.werner.bl.functionapp.codegeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FunctionAppCodeGenerationResult {

    private String archivePath;

    private String functionAppName;

    private String resourceGroupName;
}
