package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.log.PowershellTaskLogger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceBusQueuePowershellCaller extends AbstractPowershellResourceCreationCaller {

    private final String SCRIPT_NAME = "serviceBusQueueT.json";

    private final String SERVICEBUS_QUEUE_TEMPLATE = TEMPLATE_DIR + "serviceBusQueue\\";

    private final String SCRIPT = "$parameters = @{}; "
            + "$parameters.Add('serviceBusNamespaceName', '%s'); "
            + "$parameters.Add('serviceBusQueueName', '%s'); "
            + "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

    public ServiceBusQueuePowershellCaller(PowershellTaskLogger logger) {
        super(logger);
    }

    @Override
    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String sbns = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
        String sbQueueName = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_QUEUE).findFirst().get().getName();

        return String.format(SCRIPT, sbns, sbQueueName, resourceGroup, SERVICEBUS_QUEUE_TEMPLATE + SCRIPT_NAME);    }

    @Override
    protected String getScript(ResourceGroup rgNode) {
        throw new UnsupportedOperationException("Resources need a ResourceGroup to live in");
    }
}
