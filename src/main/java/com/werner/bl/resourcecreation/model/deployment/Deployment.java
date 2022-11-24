package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;

import java.util.ArrayList;
import java.util.List;

public class Deployment {
	private List<AbstractResourceNode> resourceFamily;
	private final DeploymentHandler deploymentHandler;

	public Deployment() {
		this.resourceFamily = new ArrayList<>();
		deploymentHandler = new DeploymentHandler();
	}

	public void deploy() throws Exception {
		deploymentHandler.handleDeployment(resourceFamily);
	}

	public List<AbstractResourceNode> getResourceFamily() {
		return resourceFamily;
	}

	public void setResourceFamily(List<AbstractResourceNode> resourceFamily) {
		this.resourceFamily = resourceFamily;
	}
}
