package com.werner.bl.codegeneration.generators.componentlevel.triggers;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServicebusQueueTriggerGenerator extends AbstractTriggerGenerator {

    private final String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";

    private final String PLACEHOLDER_JAVA_FUNCTION_NAME = "PLACEHOLDER_JAVA_FUNCTION_NAME";

    private final String PLACEHOLDER_QUEUE_NAME = "PLACEHOLDER_QUEUE_NAME";

    private final String PLACEHOLDER_CONNECTION_STRING = "PLACEHOLDER_CONNECTION_STRING";

    private final TemplateResolver templateResolver;

    @Override
    protected String generateTriggerCode(FunctionAppTrigger trigger) {
        String template = templateResolver.resolveTemplate(TemplateName.TRIGGER_SB_QUEUE);

        return template
                .replace(PLACEHOLDER_FUNCTION_NAME, trigger.getTriggerName())
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, trigger.getTriggerName())
                .replace(PLACEHOLDER_QUEUE_NAME, trigger.getTriggerName())
                .replace(PLACEHOLDER_CONNECTION_STRING, trigger.getTriggerParams().get("ServicebusConnection"));
    }
}
