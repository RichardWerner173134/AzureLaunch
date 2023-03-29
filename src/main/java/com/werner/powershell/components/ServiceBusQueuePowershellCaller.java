package com.werner.powershell.components;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.helper.FileUtil;
import com.werner.log.TaskLogger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class ServiceBusQueuePowershellCaller extends AbstractPowershellResourceCreationCaller {

    private final String SCRIPT_NAME = "serviceBusQueueT.json";

    private final String SERVICEBUS_QUEUE_TEMPLATE = TEMPLATE_DIR + "serviceBusQueue\\";

    private final TemplateResolver templateResolver;

    private final FileUtil fileUtil;

    private final String SCRIPT = "$parameters = @{}; "
            + "$parameters.Add('serviceBusNamespaceName', '%s'); "
            + "$parameters.Add('serviceBusQueueName', '%s'); "
            + "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

    public ServiceBusQueuePowershellCaller(TaskLogger logger, TemplateResolver templateResolver, FileUtil fileUtil) {
        super(logger);
        this.templateResolver = templateResolver;
        this.fileUtil = fileUtil;
    }

    @Override
    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) throws IOException {
        String sbns = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
        String sbQueueName = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_QUEUE).findFirst().get().getName();

        String template = templateResolver.resolveTemplate(TemplateName.SCRIPT_SERVICEBUS_QUEUE);
        Path tempFile = fileUtil.createTempFile(template);
        File file = tempFile.toFile();

        return String.format(SCRIPT, sbns, sbQueueName, resourceGroup, file.getAbsolutePath(), SERVICEBUS_QUEUE_TEMPLATE + SCRIPT_NAME);    }

    @Override
    protected String getScript(ResourceGroup rgNode) {
        throw new UnsupportedOperationException("Resources need a ResourceGroup to live in");
    }
}
