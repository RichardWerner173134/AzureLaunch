package com.werner.bl.codegeneration.generators.componentlevel;

public abstract class AbstractCodeComponentGenerator {
    protected String removeDashes(String s) {
        return s.replaceAll("-", "");
    }
}
