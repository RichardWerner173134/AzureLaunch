package com.werner.bl.functionapp.codegeneration.generators.client;

import com.werner.bl.functionapp.codegeneration.TemplateName;
import com.werner.bl.functionapp.codegeneration.TemplateResolver;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetClientGenerator extends AbstractClientCodeGenerator {
    private final TemplateResolver templateResolver;

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";

    private final String PLACEHOLDER_URL = "PLACEHOLDER_URL";

    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    @Override
    protected String generateClientCode(FunctionAppClient client) {
        String template = templateResolver.resolveTemplate(TemplateName.CLIENT_HTTP_GET);

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, removeDashes(client.getClientName()))
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, removeDashes(client.getClientName()))
                .replace(PLACEHOLDER_URL, client.getClientParams().get("url"));
    }

    @Override
    protected List<String> getImportCode() {
        return List.of(
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
        );
    }

    @Override
    protected List<String> getNecessaryDependencies() {
        return List.of(
                    "        <!-- https://mvnrepository.com/artifact/com.squareup.okhttp/okhttp -->\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.squareup.okhttp</groupId>\n" +
                        "            <artifactId>okhttp</artifactId>\n" +
                        "            <version>2.7.5</version>\n" +
                        "        </dependency>"
        );
    }
}
