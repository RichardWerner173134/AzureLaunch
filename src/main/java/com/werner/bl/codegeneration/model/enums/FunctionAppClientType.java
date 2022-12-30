package com.werner.bl.codegeneration.model.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum FunctionAppClientType {
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
                    "import com.squareup.okhttp.Call;",
                    "import com.squareup.okhttp.OkHttpClient;",
                    "import com.squareup.okhttp.Request;",
                    "import com.squareup.okhttp.Response;",
                    "import java.io.IOException;",
                    "import java.util.Optional;"
            ),
            List.of(
                    "<!-- https://mvnrepository.com/artifact/com.squareup.okhttp/okhttp -->\n" +
                            "        <dependency>\n" +
                            "            <groupId>com.squareup.okhttp</groupId>\n" +
                            "            <artifactId>okhttp</artifactId>\n" +
                            "            <version>2.7.5</version>\n" +
                            "        </dependency>"
            )),
    HTTP_POST("http-post",
            List.of(),
            List.of());

    private String name;

    private List<String> necessaryImports;

    private List<String> necessaryDependencies;

    private FunctionAppClientType(String name, List<String> necessaryImports, List<String> necessaryDependencies) {
        this.name = name;
        this.necessaryImports = necessaryImports;
        this.necessaryDependencies = necessaryDependencies;
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
