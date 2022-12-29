package com.werner.bl.functionapp.codegeneration.generators.triggers;

import com.werner.bl.functionapp.codegeneration.TemplateResolver;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.resourcecreation.model.ResourceType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.werner.bl.functionapp.codegeneration.TemplateName.TRIGGER_SB_PUB_SUB;

@Component
@AllArgsConstructor
public class ServicebusTopicTriggerGenerator extends AbstractTriggerGenerator {

    private final TemplateResolver templateResolver;

    private final static String PLACEHOLDER_TOPIC = "PLACEHOLDER_TOPIC_NAME";

    private final static String PLACEHOLDER_SUBSCRIPTION = "PLACEHOLDER_SUBSCRIPTION_NAME";

    private final static String PLACEHOLDER_CONNECTION = "PLACEHOLDER_CONNECTIONSTRING";

    private final static String PLACEHOLDER_FUNCTION_NAME = "PLACEHOLDER_FUNCTION_NAME";


    @Override
    protected String generateTriggerCode(FunctionAppTrigger trigger) {
        String template = templateResolver.resolveTemplate(TRIGGER_SB_PUB_SUB);

        String name = removeDashes(trigger.getTriggerName());
        String topic = trigger.getTriggerParams().get(ResourceType.SERVICEBUS_TOPIC.getName());
        String subscription = trigger.getTriggerParams().get(ResourceType.SERVICEBUS_SUBSCRIPTION.getName());

        return template.replace(PLACEHOLDER_FUNCTION_NAME, name)
                .replace(PLACEHOLDER_TOPIC, topic)
                .replace(PLACEHOLDER_SUBSCRIPTION, subscription)
                .replace(PLACEHOLDER_CONNECTION, "ServicebusConnection");
    }

    @Override
    protected List<String> getImportCode() {
        return List.of(
                "import com.microsoft.azure.functions.ExecutionContext;",
                "import com.microsoft.azure.functions.annotation.FunctionName;",
                "import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;"
        );
    }

    @Override
    protected List<String> getNecessaryDependencies() {
        return List.of();
    }
}
