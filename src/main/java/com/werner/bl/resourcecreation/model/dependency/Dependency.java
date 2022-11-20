package com.werner.bl.resourcecreation.model.dependency;

import com.werner.bl.resourcecreation.model.ResourceType;

public class Dependency {
	private final ResourceType dependentType;
	private final ResourceType dependencyType;

	public Dependency(ResourceType dependentType, ResourceType dependencyType) {
		this.dependentType = dependentType;
		this.dependencyType = dependencyType;
	}

	public ResourceType getDependentType() {
		return dependentType;
	}

	public ResourceType getDependencyType() {
		return dependencyType;
	}
}
