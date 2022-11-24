package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.helper.PowershellCaller;

import java.util.List;

public class DeploymentHandler {
	private final static String RESOURCE_GROUP_DEFAULT_VALUE = "rgtest-rwerner";
	private final static String TEMPLATE_DIRECTORY = "src/main/resources/templates/";
	private final static String SERVICEBUS_TEMPLATE_DIR = TEMPLATE_DIRECTORY + "serviceBusPubSub/";

	private PowershellCaller powershellCaller;

	// TODO Refactoring
	public DeploymentHandler() {
		this.powershellCaller = new PowershellCaller();
	}

	public void handleDeployment(List<AbstractResourceNode> resourceFamily) throws Exception {
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
				String scriptPath = SERVICEBUS_TEMPLATE_DIR;
				String[] params = new String[]{
						resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName(),
						resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_TOPIC).findFirst().get().getName(),
						resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.SERVICEBUS_SUBSCRIPTION).findFirst().get().getName(),
						RESOURCE_GROUP_DEFAULT_VALUE
				};

				powershellCaller.executeScriptWithParams(scriptPath, params);
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
