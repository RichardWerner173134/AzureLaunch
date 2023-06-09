package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.exception.InvalidInputFileContentException;
import lombok.Getter;

@Getter
public enum EdgeType {

    // connection between two functionApps

    HTTP_GET("http-get"),

    HTTP_POST("http-post"),

    // connection between a functionapp and a trigger/output binding
    WRITE("write"),

    READ("read"),

    // TODO delete
    SERVICE_BUS_PUB_SUB_READ("sb-pub-sub-read"),

    SERVICE_BUS_QUEUE_READ("sb-queue-read")
    ;

    private String name;

    EdgeType(String name) {
        this.name = name;
    }

    public static EdgeType findById(String id) throws InvalidInputFileContentException {
        for (EdgeType value : values()) {
            if(id.equals(value.getName())) {
                return value;
            }
        }

        throw new InvalidInputFileContentException("Invalid InputFile. Unknown EdgeType: " + id);
    }
}
