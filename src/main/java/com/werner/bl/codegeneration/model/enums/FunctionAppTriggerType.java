package com.werner.bl.codegeneration.model.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum FunctionAppTriggerType {
    HTTP_GET("http-get",
            List.of(
                    "import com.microsoft.azure.functions.ExecutionContext;",
                    "import com.microsoft.azure.functions.HttpMethod;",
                    "import com.microsoft.azure.functions.HttpRequestMessage;",
                    "import com.microsoft.azure.functions.HttpResponseMessage;",
                    "import com.microsoft.azure.functions.HttpStatus;",
                    "import com.microsoft.azure.functions.annotation.AuthorizationLevel;",
                    "import com.microsoft.azure.functions.annotation.FunctionName;",
                    "import com.microsoft.azure.functions.annotation.HttpTrigger;",
                    "import java.util.Optional;"
            ),
            List.of()
    ),
    HTTP_POST("http-post",
            List.of(),
            List.of()
    ),
    SERVICE_BUS_PUB_SUB("sb-pub-sub",
            List.of(
                    "import com.microsoft.azure.functions.ExecutionContext;",
                    "import com.microsoft.azure.functions.annotation.FunctionName;",
                    "import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;"
            ),
            List.of()
    ),
    SERVICE_BUS_QUEUE("sb-queue",
            List.of(),
            List.of()
    );

    private String name;

    private List<String> necessaryImports;

    private List<String> necessaryDependencies;

    private FunctionAppTriggerType(String name, List<String> necessaryImports, List<String> necessaryDependencies) {
        this.name = name;
        this.necessaryImports = necessaryImports;
        this.necessaryDependencies = necessaryDependencies;
    }

    public static FunctionAppTriggerType findById(String id) {
        for (FunctionAppTriggerType value : values()) {
            if (id.equals(value.getName())) {
                return value;
            }
        }

        throw new UnsupportedOperationException("Id not found: " + id);
    }
}
