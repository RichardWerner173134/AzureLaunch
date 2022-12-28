package com.werner.bl.functionapp.codegeneration.generators;

public abstract class AbstractCodeComponentGenerator {
    protected String removeDashes(String s) {
        return s.replaceAll("-", "");
    }
}
