package com.werner.bl.codegeneration.helper;

import lombok.Getter;

@Getter
public enum TemplateName {

    FUNCTION_APP_BASE_CLASS("FunctionAppBaseClass.java"),

    POM("PomTemplate.xml"),

    LOCAL_SETTINGS("LocalSettingsTemplate.json"),

    CLIENT_HTTP_GET("clients/HttpClientGet.java"),

    CLIENT_HTTP_POST("clients/HttpClientPost.java"),

    TRIGGER_SB_QUEUE("triggers/SbQueueTrigger.java"),

    TRIGGER_SB_PUB_SUB("triggers/SbPubSubTrigger.java"),

    TRIGGER_HTTP_GET("triggers/HttpGetTrigger.java"),

    TRIGGER_HTTP_POST("triggers/HttpPostTrigger.java"),
    ;


    private String filepath;

    private TemplateName(String filepath) {
        final String templateDir = "src/main/resources/templates/code/";

        this.filepath = templateDir + filepath;
    }
}
