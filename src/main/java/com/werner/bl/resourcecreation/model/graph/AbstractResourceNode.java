package com.werner.bl.resourcecreation.model.graph;

public abstract class AbstractResourceNode {
	private String name;
	private String resourceType;

	public AbstractResourceNode(String name, String resourceType) {
		this.name = name;
		this.resourceType = resourceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}
