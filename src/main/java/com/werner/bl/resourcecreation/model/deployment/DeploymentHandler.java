package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.ResourceGroup;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.components.FunctionAppPowershellCaller;
import com.werner.powershell.components.ResourceGroupPowershellCaller;
import com.werner.powershell.components.ServiceBusSubscriptionPowershellCaller;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


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

		if(deployment.getResourceFamily().get(0) instanceof ResourceGroup) {
			rgPSCaller.createResourceGroup((ResourceGroup) deployment.getResourceFamily().get(0));
			cachedRgNameForCurrentDeployments = ((ResourceGroup) deployment.getResourceFamily().get(0)).getResourceGroupName();
		} else {
			List<AbstractResourceNode> castedList = deployment.getResourceFamily().stream()
					.map(a -> (AbstractResourceNode) a)
					.collect(Collectors.toList());

			switch (castedList.get(0).getResourceType()) {
				case FUNCTION:
				case FUNCTION_APP:
					funAppPSCaller.createResourceInResourceGroup(castedList, cachedRgNameForCurrentDeployments);
					break;
				case KEYVAULT:
					break;
				case KEYVAULT_SECRET:
					break;
				case SERVICEBUS_SUBSCRIPTION:
				case SERVICEBUS_TOPIC:
				case SERVICEBUS_NAMESPACE:
					sbsubPSCaller.createResourceInResourceGroup(castedList, cachedRgNameForCurrentDeployments);
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
}
