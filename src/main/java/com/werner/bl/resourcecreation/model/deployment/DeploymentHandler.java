package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.exception.AzureResourceCreationFailedException;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.components.ResourceGroupPowershellCaller;
import com.werner.powershell.components.ServiceBusQueuePowershellCaller;
import com.werner.powershell.components.ServiceBusSubscriptionPowershellCaller;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DeploymentHandler {

	private ServiceBusQueuePowershellCaller serviceBusQueuePowershellCaller;

	private String cachedRgNameForCurrentDeployments;

	private ResourceGroupPowershellCaller rgPSCaller;

	private ServiceBusSubscriptionPowershellCaller serviceBusSubscriptionPowershellCaller;

	public DeploymentHandler(ServiceBusQueuePowershellCaller serviceBusQueuePowershellCaller, ResourceGroupPowershellCaller rgPSCaller,
							 ServiceBusSubscriptionPowershellCaller serviceBusSubscriptionPowershellCaller) {
		this.serviceBusQueuePowershellCaller = serviceBusQueuePowershellCaller;
		this.rgPSCaller = rgPSCaller;
		this.serviceBusSubscriptionPowershellCaller = serviceBusSubscriptionPowershellCaller;
	}

	public void deploy(Deployment deployment) throws AzureResourceCreationFailedException, IOException {

		AbstractResourceNode firstResource = deployment.getDeploymentComposite().get(0);

		switch (firstResource.getResourceType()) {
			case RESOURCE_GROUP:
				rgPSCaller.createResourceGroup((ResourceGroup) firstResource);
				cachedRgNameForCurrentDeployments = firstResource.getName();
				break;
			case SERVICEBUS_NAMESPACE:
			case SERVICEBUS_TOPIC:
			case SERVICEBUS_SUBSCRIPTION:
				serviceBusSubscriptionPowershellCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments);
				break;
			case SERVICEBUS_QUEUE:
				serviceBusQueuePowershellCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments);
				break;
		}
	}
}
