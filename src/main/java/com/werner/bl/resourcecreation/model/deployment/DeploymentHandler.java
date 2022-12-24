package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.components.FunctionAppPowershellCaller;
import com.werner.powershell.components.ResourceGroupPowershellCaller;
import com.werner.powershell.components.ServiceBusSubscriptionPowershellCaller;
import org.springframework.stereotype.Component;


@Component
public class DeploymentHandler {

	private String cachedRgNameForCurrentDeployments;

	private ResourceGroupPowershellCaller rgPSCaller;

	private ServiceBusSubscriptionPowershellCaller sbsubPSCaller;

	private FunctionAppPowershellCaller funAppPSCaller;

	public DeploymentHandler(ResourceGroupPowershellCaller rgPSCaller,
							 ServiceBusSubscriptionPowershellCaller sbsubPSCaller,
							 FunctionAppPowershellCaller funAppPSCaller) {
		this.rgPSCaller = rgPSCaller;
		this.sbsubPSCaller = sbsubPSCaller;
		this.funAppPSCaller = funAppPSCaller;
	}

	public void handleDeployment(Deployment deployment) throws Exception {

		AbstractResourceNode firstResource = deployment.getDeploymentComposite().get(0);

		switch (firstResource.getResourceType()) {
			case RESOURCE_GROUP:
				rgPSCaller.createResourceGroup((ResourceGroup) firstResource);
				cachedRgNameForCurrentDeployments = firstResource.getName();
				break;
			case FUNCTION:
			case FUNCTION_APP:
				funAppPSCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments);
				break;
			case KEYVAULT:
				break;
			case KEYVAULT_SECRET:
				break;
			case SERVICEBUS_SUBSCRIPTION:
			case SERVICEBUS_TOPIC:
			case SERVICEBUS_NAMESPACE:
				sbsubPSCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments);
				break;
			case VNET:
				break;
			case STORAGE_ACCOUNT:
				break;
			case APP_SERVICE_ENVIRONMENT:
				break;
		}
	}
}
