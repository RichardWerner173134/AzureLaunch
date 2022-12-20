package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;

import java.util.ArrayList;
import java.util.List;

public class Deployment {
	private List<AbstractResourceNode> resourceFamily;

	public Deployment() {
		this.resourceFamily = new ArrayList<>();
	}

	public List<AbstractResourceNode> getResourceFamily() {
		return resourceFamily;
	}

	public void setResourceFamily(List<AbstractResourceNode> resourceFamily) {
		this.resourceFamily = resourceFamily;
	}
}
