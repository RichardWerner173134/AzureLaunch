package com.werner.bl.functionapp.codegeneration.generators.client;

import com.werner.bl.functionapp.codegeneration.TemplateName;
import com.werner.bl.functionapp.codegeneration.TemplateResolver;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GetClientGenerator extends AbstractClientCodeGenerator {
    private final TemplateResolver templateResolver;

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";

    private final String PLACEHOLDER_URL = "PLACEHOLDER_URL";

    @Override
    public String generateClientCode(FunctionAppClient client) {
        String template = templateResolver.resolveTemplate(TemplateName.CLIENT_HTTP_GET);

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, removeDashes(client.getClientName()))
                .replace(PLACEHOLDER_URL, client.getClientParams().get("url"));
    }

    @Override
    protected List<String> getImportCode() {
        return new ArrayList<>();
    }
}
