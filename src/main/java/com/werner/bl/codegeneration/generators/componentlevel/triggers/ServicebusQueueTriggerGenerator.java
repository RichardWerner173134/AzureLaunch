package com.werner.bl.codegeneration.generators.componentlevel.triggers;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.codegeneration.model.enums.TriggerParam;
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

        String name = trigger.getTriggerName();
        String queueName = trigger.getTriggerParams().get(TriggerParam.P_SERVICEBUS_QUEUE_NAME.getValue());

        String connectionStringAppSettingsKey = name + TriggerParam.AS_CONNECTIONSTRING.getValue();

        return template
                .replaceAll(PLACEHOLDER_FUNCTION_NAME, name)
                .replace(PLACEHOLDER_JAVA_FUNCTION_NAME, name)
                .replace(PLACEHOLDER_QUEUE_NAME, queueName)
                .replace(PLACEHOLDER_CONNECTION_STRING, connectionStringAppSettingsKey);
    }
}
