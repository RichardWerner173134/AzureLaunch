package com.werner.bl.codegeneration.generators.componentlevel.triggers;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class HttpGetTriggerGenerator extends AbstractTriggerGenerator {
    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";
    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    private final TemplateResolver templateResolver;

    @Override
    protected String generateTriggerCode(FunctionAppTrigger trigger) {
        String template = templateResolver.resolveTemplate(TemplateName.TRIGGER_HTTP_GET);

        String name = trigger.getTriggerName();

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, name)
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, name);
    }
}
