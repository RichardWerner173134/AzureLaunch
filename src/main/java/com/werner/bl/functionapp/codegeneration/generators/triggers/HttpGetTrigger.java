package com.werner.bl.functionapp.codegeneration.generators.triggers;

import com.werner.bl.functionapp.codegeneration.TemplateName;
import com.werner.bl.functionapp.codegeneration.TemplateResolver;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class HttpGetTrigger extends AbstractTriggerGenerator {
    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";
    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    private final TemplateResolver templateResolver;

    @Override
    protected String generateTriggerCode(FunctionAppTrigger trigger) {
        String template = templateResolver.resolveTemplate(TemplateName.TRIGGER_HTTP_GET);

        return template
                .replace(PLACEHOLDER_FUNCTION_NAME, removeDashes(trigger.getTriggerName()))
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, removeDashes(trigger.getTriggerName()));
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
                "import java.util.Optional;"
        );
    }

    @Override
    protected List<String> getNecessaryDependencies() {
        return List.of();
    }
}
