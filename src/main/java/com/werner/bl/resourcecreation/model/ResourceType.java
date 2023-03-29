package com.werner.bl.resourcecreation.model;

import com.werner.bl.exception.InvalidInputFileContentException;
import lombok.Getter;

@Getter
public enum ResourceType {

	SERVICEBUS_NAMESPACE("Microsoft.ServiceBus/namespaces", "sbns"),

	SERVICEBUS_TOPIC("Microsoft.ServiceBus/namespaces/topics", "sbtop"),

	SERVICEBUS_SUBSCRIPTION("Microsoft.ServiceBus/namespaces/topics/Subscriptions", "sbsub"),

	SERVICEBUS_QUEUE("Microsoft.ServiceBus/namespaces/queues", "sbqueue"),

	FUNCTION_APP("Microsoft.Web/sites", "funcapp"),

	FUNCTION("Microsoft.Web/sites/functions", "func"),

	RESOURCE_GROUP("ResourceGroup", "rg"),
	;

	private String name;
	private String shortName;

	ResourceType(String name, String shortName) {
		this.name = name;
		this.shortName = shortName;
	}

	public static ResourceType findById(String id) throws InvalidInputFileContentException {
		for(ResourceType t : ResourceType.values()) {
			if(t.name.equals(id)) {
				return t;
			}
		}
		throw new InvalidInputFileContentException("Invalid Inputfile. Unknown AzureResourceType: " + id);
	}
}