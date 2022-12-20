package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.components.ServiceBusSubscriptionPowershellCaller;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DeploymentHandler {
	private final static String RESOURCE_GROUP_DEFAULT_VALUE = "rgtest-rwerner";

	private ServiceBusSubscriptionPowershellCaller sbsubCaller;

	public void handleDeployment(Deployment deployment) throws Exception {
		List<AbstractResourceNode> resourceFamily = deployment.getResourceFamily();
		switch (resourceFamily.get(0).getResourceType()) {
			case FUNCTION:
				break;
			case KEYVAULT:
				break;
			case KEYVAULT_SECRET:
				break;
			case SERVICEBUS_SUBSCRIPTION:
			case SERVICEBUS_TOPIC:
			case SERVICEBUS_NAMESPACE:
				sbsubCaller.executeScriptWithParams(resourceFamily, RESOURCE_GROUP_DEFAULT_VALUE);
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
