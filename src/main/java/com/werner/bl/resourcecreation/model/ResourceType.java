package com.werner.bl.resourcecreation.model;

public enum ResourceType {

	SERVICEBUS_NAMESPACE("Microsoft.ServiceBus/namespaces", "sbns"),
	SERVICEBUS_TOPIC("Microsoft.ServiceBus/namespaces/topics", "sbtop"),
	SERVICEBUS_SUBSCRIPTION("Microsoft.ServiceBus/namespaces/topics/Subscriptions", "sbsub"),
	STORAGE_ACCOUNT("Microsoft.Storage/storageAccounts", "storacc"),
	FUNCTION_APP("Microsoft.Web/sites", "funcapp"),
	FUNCTION("Microsoft.Web/sites/functions", "func"),
	APP_SERVICE_ENVIRONMENT("Microsoft.Web/hostingEnvironments", "ase"),
	VNET("Microsoft.Network/virtualNetworks", "vnet"),
	KEYVAULT("Microsoft.KeyVault/vaults", "kv"),
	KEYVAULT_SECRET("Microsoft.KeyVault/vaults/secrets", "kvsec");

	private String name;
	private String shortName;

	ResourceType(String name, String shortName) {
		this.name = name;
		this.shortName = shortName;
	}

	public static ResourceType findById(String id){
		for(ResourceType t : ResourceType.values()) {
			if(t.name.equals(id)) {
				return t;
			}
		}
		return null;
	}

	public String getShortName(){
		return shortName;
	}
}
