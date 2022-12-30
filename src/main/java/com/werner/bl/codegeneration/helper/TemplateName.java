package com.werner.bl.codegeneration.helper;

import lombok.Getter;

@Getter
public enum TemplateName {

    FUNCTION_APP_BASE_CLASS("FunctionAppBaseClass.java"),

    CLIENT_HTTP_GET("clients/FunctionClientHttpGet.java"),

    TRIGGER_SB_PUB_SUB("triggers/SbPubSubTrigger.java"),

    TRIGGER_HTTP_GET("triggers/HttpGetTrigger.java"),

    POM_WITH_PLACEHOLDERS("PomTemplate.xml")
    ;


    private String filepath;

    private TemplateName(String filepath) {
        final String templateDir = "src/main/resources/templates/code/";

        this.filepath = templateDir + filepath;
    }
}
