package com.werner.bl.resourcecreation.model.dependency;

import com.werner.bl.resourcecreation.model.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class DependencyHierarchy {
	private final List<Dependency> dependencyList;

	public DependencyHierarchy() {
		this.dependencyList = new ArrayList<>();

		dependencyList.addAll(List.of(
				new Dependency(ResourceType.FUNCTION, ResourceType.FUNCTION_APP),
				new Dependency(ResourceType.KEYVAULT_SECRET, ResourceType.KEYVAULT),
				new Dependency(ResourceType.SERVICEBUS_SUBSCRIPTION, ResourceType.SERVICEBUS_TOPIC),
				new Dependency(ResourceType.SERVICEBUS_TOPIC, ResourceType.SERVICEBUS_NAMESPACE))
		);
	}
}
