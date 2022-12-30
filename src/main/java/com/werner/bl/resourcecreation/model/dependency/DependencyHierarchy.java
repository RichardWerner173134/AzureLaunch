package com.werner.bl.resourcecreation.model.dependency;

import com.werner.bl.resourcecreation.model.ResourceType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DependencyHierarchy {
	private final List<Dependency> dependencyList;

	public DependencyHierarchy() {
		this.dependencyList = new ArrayList<>();

		dependencyList.addAll(List.of(
				new Dependency(ResourceType.SERVICEBUS_SUBSCRIPTION, ResourceType.SERVICEBUS_TOPIC),
				new Dependency(ResourceType.SERVICEBUS_TOPIC, ResourceType.SERVICEBUS_NAMESPACE))
		);
	}

	public List<Dependency> getDependencyList() {
		return dependencyList;
	}

	public Dependency findByType(ResourceType type){

		for (Dependency dependency : dependencyList) {
			if (type == dependency.getDependentType()){
				return dependency;
			}
		}
		return null;
	}
}
