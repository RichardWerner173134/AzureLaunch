package com.werner.bl.codegeneration.helper;

import lombok.Getter;

@Getter
public enum TemplateName {

    FUNCTION_APP_BASE_CLASS("code/FunctionAppBaseClass.java"),

    POM("code/PomTemplate.xml"),

    LOCAL_SETTINGS("code/LocalSettingsTemplate.json"),

    CLIENT_HTTP_GET("code/clients/HttpClientGet.java"),

    CLIENT_HTTP_POST("code/clients/HttpClientPost.java"),

    TRIGGER_SB_QUEUE("code/triggers/SbQueueTrigger.java"),

    TRIGGER_SB_PUB_SUB("code/triggers/SbPubSubTrigger.java"),

    TRIGGER_HTTP_GET("code/triggers/HttpGetTrigger.java"),

    TRIGGER_HTTP_POST("code/triggers/HttpPostTrigger.java"),

    SCRIPT_SERVICE_PRINCIPAL("servicePrincipal/servicePrincipalScript.ps1")
    ;


    private String filepath;

    private TemplateName(String filepath) {
        final String templateDir = "src/main/resources/templates/";

        this.filepath = templateDir + filepath;
    }
}
