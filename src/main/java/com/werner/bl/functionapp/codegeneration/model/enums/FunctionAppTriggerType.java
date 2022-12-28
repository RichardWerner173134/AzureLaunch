package com.werner.bl.functionapp.codegeneration.model.enums;

import lombok.Getter;

@Getter
public enum FunctionAppTriggerType {
    HTTP_GET("http-get"),
    HTTP_POST("http-post"),
    SERVICE_BUS_PUB_SUB("sb-pub-sub"),
    SERVICE_BUS_QUEUE("sb-queue")
    ;

    private String name;

    private FunctionAppTriggerType(String name) {
        this.name = name;
    }

    public static FunctionAppTriggerType findById(String id) {
        for (FunctionAppTriggerType value : values()) {
            if(id.equals(value.getName())) {
                return value;
            }
        }

        throw new UnsupportedOperationException("Id not found: " + id);
    }
}
