package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.resourcecreation.model.ResourceType;

public abstract class AbstractResourceNode {
	private String name;
	private ResourceType resourceType;

	public AbstractResourceNode(String name, ResourceType resourceType) {
		this.name = name;
		this.resourceType = resourceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
}
