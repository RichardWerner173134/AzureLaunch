package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.AbstractPowershellCaller;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceBusSubscriptionPowershellCaller extends AbstractPowershellCaller {
    private final String SCRIPT = "$parameters = @{}; "
            + "$parameters.Add('service_BusNamespace_Name', '%s'); "
            + "$parameters.Add('serviceBusTopicName', '%s'); "
            + "$parameters.Add('serviceBusSubscriptionName', '%s'); "
            + "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

    private final String SCRIPT_NAME = "serviceBusT.json";

    private final String SERVICEBUS_TEMPLATE_DIR = TEMPLATE_DIRECTORY + "serviceBusPubSub/";

    @Override
    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String sbns = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
        String sbtopic = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_TOPIC).findFirst().get().getName();
        String sbsub = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_SUBSCRIPTION).findFirst().get().getName();

        return String.format(SCRIPT, sbns, sbtopic, sbsub, resourceGroup, SERVICEBUS_TEMPLATE_DIR + SCRIPT_NAME);
    }
}
