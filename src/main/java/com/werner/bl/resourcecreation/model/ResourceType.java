package com.werner.bl.resourcecreation.model;

public enum ResourceType {

	SERVICEBUS_NAMESPACE("Microsoft.ServiceBus/namespaces"),
	SERVICEBUS_TOPIC("Microsoft.ServiceBus/namespaces/topics"),
	SERVICEBUS_SUBSCRIPTION("Microsoft.ServiceBus/namespaces/topics/Subscriptions"),
	STORAGE_ACCOUNT("Microsoft.Storage/storageAccounts"),
	FUNCTION_APP("Microsoft.Web/sites"),
	FUNCTION("Microsoft.Web/sites/functions"),
	APP_SERVICE_ENVIRONMENT("Microsoft.Web/hostingEnvironments"),
	VNET("Microsoft.Network/virtualNetworks"),
	KEYVAULT("Microsoft.KeyVault/vaults"),
	KEYVAULT_SECRET("Microsoft.KeyVault/vaults/secrets");

	private String name;

	ResourceType(String name) {
		this.name = name;
	}

	public static ResourceType findById(String id){
		for(ResourceType t : ResourceType.values()) {
			if(t.name.equals(id)) {
				return t;
			}
		}
		return null;
	}
}
