package com.werner.bl.codegeneration.generators.componentlevel.triggers;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HttpPostTriggerGenerator extends AbstractTriggerGenerator {

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";
    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    private final TemplateResolver templateResolver;

    @Override
    protected String generateTriggerCode(FunctionAppTrigger trigger) {
        String template = templateResolver.resolveTemplate(TemplateName.TRIGGER_HTTP_POST);

        return template
                .replace(PLACEHOLDER_FUNCTION_NAME, removeDashes(trigger.getTriggerName()))
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, removeDashes(trigger.getTriggerName()));
    }
}
