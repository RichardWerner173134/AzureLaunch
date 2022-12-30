package com.werner.bl.codegeneration.generators.classlevel;

import com.werner.bl.codegeneration.generators.componentlevel.client.HttpGetClientGenerator;
import com.werner.bl.codegeneration.generators.componentlevel.triggers.HttpGetTriggerGenerator;
import com.werner.bl.codegeneration.generators.componentlevel.triggers.ServicebusTopicTriggerGenerator;
import com.werner.bl.codegeneration.generators.projectlevel.Project;
import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.codegeneration.model.enums.FunctionAppClientType;
import com.werner.bl.codegeneration.model.enums.FunctionAppTriggerType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@AllArgsConstructor
public class ClassGenerator {

    private final String PLACEHOLDER_CODE = "PLACEHOLDER_CODE";

    private final String PLACEHOLDER_IMPORT = "PLACEHOLDER_IMPORT";

    private final String PLACEHOLDER_PACKAGE = "PLACEHOLDER_PACKAGE";

    private final TemplateResolver templateResolver;

    private final ServicebusTopicTriggerGenerator servicebusTopicTriggerGenerator;

    private final HttpGetClientGenerator httpGetClientGenerator;

    private final HttpGetTriggerGenerator httpGetTriggerGenerator;

    public String generateClassCode(List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients, Project project) {
        String classTemplate = templateResolver.resolveTemplate(TemplateName.FUNCTION_APP_BASE_CLASS);
        String packageStatement = "package " + project.getGroupId() + "." + project.getArtifactId() + ";";

        HashSet<String> imports = new LinkedHashSet<>();

        String result = classTemplate;
        for (FunctionAppTrigger trigger : triggers) {
            String code = getTriggerCode(trigger);
            result = result.replace(PLACEHOLDER_CODE, code + PLACEHOLDER_CODE);
            imports.addAll(trigger.getTriggerType().getNecessaryImports());
        }

        for (FunctionAppClient client : clients) {
            String code = getClientCode(client);
            result = result.replace(PLACEHOLDER_CODE, code + PLACEHOLDER_CODE);
            imports.addAll(client.getClientType().getNecessaryImports());
        }

        for (String i : imports) {
            result = result.replace(PLACEHOLDER_IMPORT, i + "\n" + PLACEHOLDER_IMPORT);
        }

        return result
                .replace(PLACEHOLDER_PACKAGE, packageStatement)
                .replace(PLACEHOLDER_IMPORT, "")
                .replace(PLACEHOLDER_CODE, "");
    }

    private String getClientCode(FunctionAppClient client) {
        FunctionAppClientType clientType = client.getClientType();
        switch(clientType) {
            case HTTP_GET:
                return httpGetClientGenerator.generateCodeString(client);
            case HTTP_POST:
        }

        throw new RuntimeException("Cannot create Client");
    }

    private String getTriggerCode(FunctionAppTrigger trigger) {
        FunctionAppTriggerType triggerType = trigger.getTriggerType();
        switch (triggerType) {
            case HTTP_GET:
                return httpGetTriggerGenerator.generateCodeString(trigger);
            case HTTP_POST:
            case SERVICE_BUS_PUB_SUB:
                return servicebusTopicTriggerGenerator.generateCodeString(trigger);
            case SERVICE_BUS_QUEUE:
        }
        throw new RuntimeException("Cannot create Trigger");
    }
}
