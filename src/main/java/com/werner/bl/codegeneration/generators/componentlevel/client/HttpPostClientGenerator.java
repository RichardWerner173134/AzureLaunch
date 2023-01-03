package com.werner.bl.codegeneration.generators.componentlevel.client;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HttpPostClientGenerator extends AbstractClientCodeGenerator {

    private final TemplateResolver templateResolver;

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";

    private final String PLACEHOLDER_URL = "PLACEHOLDER_URL";

    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    private final String PLACEHOLDER_JSON = "PLACEHOLDER_JSON";

    @Override
    protected String generateClientCode(FunctionAppClient client) {
        String template = templateResolver.resolveTemplate(TemplateName.CLIENT_HTTP_POST);

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, removeDashes(client.getClientName()))
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, removeDashes(client.getClientName()))
                .replace(PLACEHOLDER_URL, client.getClientParams().get("json"))
                .replace(PLACEHOLDER_JSON, client.getClientParams().get("json"));
    }
}
