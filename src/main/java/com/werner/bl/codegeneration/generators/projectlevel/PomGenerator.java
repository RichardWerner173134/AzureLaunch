package com.werner.bl.codegeneration.generators.projectlevel;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class PomGenerator {

    private final TemplateResolver templateResolver;

    private final String PLACEHOLDER_FUNCTION_APP_NAME = "PLACEHOLDER_FUNCTION_APP_NAME";
    private final String PLACEHOLDER_APP_SERVICE_PLAN_NAME = "PLACEHOLDER_APP_SERVICE_PLAN_NAME";
    private final String PLACEHOLDER_RESOURCE_GROUP_NAME = "PLACEHOLDER_RESOURCE_GROUP_NAME";
    private final String PLACEHOLDER_ADDITIONAL_DEPENDENCIES = "PLACEHOLDER_ADDITIONAL_DEPENDENCIES";

    private final String PLACEHOLDER_ADDITIONAL_PROPERTIES = "PLACEHOLDER_ADDITIONAL_PROPERTIES";

    private final String PLACEHOLDER_APPSETTINGS_KEY = "PLACEHOLDER_KEY";

    private final String PLACEHOLDER_APPSETTINGS_VALUE = "PLACEHOLDER_VALUE";


    public String generateCode(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
        String result = templateResolver.resolveTemplate(TemplateName.POM);

        result = result.replaceAll(PLACEHOLDER_FUNCTION_APP_NAME, project.getArtifactId())
                .replace(PLACEHOLDER_APP_SERVICE_PLAN_NAME, FunctionApp.APP_SERVICE_PLAN_NAME)
                .replace(PLACEHOLDER_RESOURCE_GROUP_NAME, FunctionApp.RESOURCE_GROUP_NAME);

        result = insertDependencies(result, triggers, clients);
        result = result.replace(PLACEHOLDER_ADDITIONAL_DEPENDENCIES, "");


        String property = "\t\t\t\t\t\t<property>\n\t\t\t\t\t\t\t<name>PLACEHOLDER_KEY</name>\n\t\t\t\t\t\t\t<value>PLACEHOLDER_VALUE</value>\n\t\t\t\t\t\t</property>\n";

        for (FunctionAppTrigger trigger : triggers) {
            for (Map.Entry<String, String> entry : trigger.getTriggerParams().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if(k.startsWith(trigger.getTriggerName())) {
                    String appSetting = property.replace(PLACEHOLDER_APPSETTINGS_KEY, k)
                            .replace(PLACEHOLDER_APPSETTINGS_VALUE, v);
                    result = result.replace(PLACEHOLDER_ADDITIONAL_PROPERTIES, appSetting + PLACEHOLDER_ADDITIONAL_PROPERTIES);
                }
            }
        }

        for (FunctionAppClient client : clients) {
            for (Map.Entry<String, String> entry : client.getClientParams().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if(k.startsWith(client.getClientName())) {
                    String appSetting = property.replace(PLACEHOLDER_APPSETTINGS_KEY, k)
                            .replace(PLACEHOLDER_APPSETTINGS_VALUE, v);
                    result = result.replace(PLACEHOLDER_ADDITIONAL_PROPERTIES,
                            appSetting + "\n" + PLACEHOLDER_ADDITIONAL_PROPERTIES);
                }
            }
        }

        return result.replace(PLACEHOLDER_ADDITIONAL_PROPERTIES, "");
    }

    private String insertDependencies(String result, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
        HashSet<String> dependencies = new LinkedHashSet<>();

        for (FunctionAppTrigger trigger : triggers) {
            dependencies.addAll(trigger.getTriggerType().getNecessaryDependencies());
        }

        for (FunctionAppClient client : clients) {
            dependencies.addAll(client.getClientType().getNecessaryDependencies());
        }

        for (String dependency : dependencies) {
            result = result.replace(PLACEHOLDER_ADDITIONAL_DEPENDENCIES, dependency + "\n" + PLACEHOLDER_ADDITIONAL_DEPENDENCIES);
        }

        return result;
    }

}
