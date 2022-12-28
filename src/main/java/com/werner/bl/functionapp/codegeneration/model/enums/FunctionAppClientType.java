package com.werner.bl.functionapp.codegeneration.model.enums;

import lombok.Getter;

@Getter
public enum FunctionAppClientType {
    HTTP_GET("http-get"),
    HTTP_POST("http-post");

    private String name;

    private FunctionAppClientType(String name) {
        this.name = name;
    }

    public static FunctionAppClientType findById(String id) {
        for (FunctionAppClientType value : values()) {
            if(id.equals(value.getName())) {
                return value;
            }
        }
        throw new UnsupportedOperationException("FunctionAppClientType Id not found: " + id);
    }
}
