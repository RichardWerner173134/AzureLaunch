package com.werner.bl.resourcecreation.model.graph.node;

import lombok.Getter;

@Getter
public enum EdgeType {
    HTTP_GET("http-get"),
    HTTP_POST("http-post"),
    SERVICE_BUS_PUB_SUB("sb-pub-sub"),

    SERVICE_BUS_QUEUE("sb-queue")
    ;

    private String name;

    private EdgeType(String name) {
        this.name = name;
    }

    public static EdgeType findById(String id) {
        for (EdgeType value : values()) {
            if(id.equals(value.getName())) {
                return value;
            }
        }

        throw new UnsupportedOperationException("Id not found: " + id);
    }
}
