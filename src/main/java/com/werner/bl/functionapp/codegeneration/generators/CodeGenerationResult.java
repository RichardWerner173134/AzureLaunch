package com.werner.bl.functionapp.codegeneration.generators;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CodeGenerationResult {

    private String code;

    private List<String> necessaryImports;
}
