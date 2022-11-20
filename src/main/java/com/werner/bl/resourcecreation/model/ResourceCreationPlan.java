package com.werner.bl.resourcecreation.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceCreationPlan {
	private List<String> creationCommands = new ArrayList<>();

	public List<String> getCreationCommands() {
		return creationCommands;
	}

	public void setCreationCommands(List<String> creationCommands) {
		this.creationCommands = creationCommands;
	}
}
