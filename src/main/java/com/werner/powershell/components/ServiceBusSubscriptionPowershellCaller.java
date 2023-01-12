package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceBusSubscriptionPowershellCaller extends AbstractPowershellResourceCreationCaller {
    private final String SCRIPT = "$parameters = @{}; "
            + "$parameters.Add('serviceBusNamespaceName', '%s'); "
            + "$parameters.Add('serviceBusTopicName', '%s'); "
            + "$parameters.Add('serviceBusSubscriptionName', '%s'); "
            + "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

    private final String SCRIPT_NAME = "serviceBusPubSub.json";

    private final String SERVICEBUS_PUB_SUB_TEMPLATE_DIR = TEMPLATE_DIR + "serviceBusPubSub\\";

    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String sbns = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
        String sbtopic = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_TOPIC).findFirst().get().getName();
        String sbsub = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_SUBSCRIPTION).findFirst().get().getName();

        return String.format(SCRIPT, sbns, sbtopic, sbsub, resourceGroup, SERVICEBUS_PUB_SUB_TEMPLATE_DIR + SCRIPT_NAME);
    }

    protected String getScript(ResourceGroup rgNode) {
        throw new UnsupportedOperationException("Resources need a ResourceGroup to live in");
    }
}
