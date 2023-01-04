package com.werner.bl.codegeneration.generators.componentlevel.client;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.enums.ClientParam;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HttpGetClientGenerator extends AbstractClientCodeGenerator {
    private final TemplateResolver templateResolver;

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";

    private final String PLACEHOLDER_URL = "PLACEHOLDER_URL";

    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    @Override
    protected String generateClientCode(FunctionAppClient client) {
        String template = templateResolver.resolveTemplate(TemplateName.CLIENT_HTTP_GET);

        String connectionString = client.getClientParams().get(client.getClientName() + ClientParam.AS_TARGET_URL.getValue());

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, client.getClientName())
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, client.getClientName())
                .replace(PLACEHOLDER_URL, connectionString);
    }
}
