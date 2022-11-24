package com.werner.bl.resourcecreation.model;

import com.werner.bl.resourcecreation.model.deployment.Deployment;

import java.util.ArrayList;
import java.util.List;

public class ResourceCreationPlan {
	private List<Deployment> deployments;

	public ResourceCreationPlan() {
		this.deployments = new ArrayList<>();
	}

	public List<Deployment> getDeployments() {
		return deployments;
	}

	public void setDeployments(List<Deployment> deployments) {
		this.deployments = deployments;
	}
}
