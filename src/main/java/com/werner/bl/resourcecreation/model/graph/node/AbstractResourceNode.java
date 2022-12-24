package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.resourcecreation.model.ResourceType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractResourceNode {
	protected String name;

	protected ResourceType resourceType;

	public String getName() {
		return name;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
}
