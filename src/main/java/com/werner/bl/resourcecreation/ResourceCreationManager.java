package com.werner.bl.resourcecreation;

import com.werner.bl.input.generated.ParseFileRequest;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceGraph;
import com.werner.helper.PowershellCaller;

public class ResourceCreationManager {
	private final PowershellCaller powershellCaller;

	public ResourceCreationManager() {
		powershellCaller = new PowershellCaller();
	}

	public ResourceGraph computeResourceGraph(ParseFileRequest request) {
		return new ResourceGraph();
	}

	public ResourceCreationPlan computeResourceCreationPlan(ResourceGraph resourceGraph) {
		ResourceCreationPlan resourceCreationPlan = new ResourceCreationPlan();
		resourceCreationPlan.getCreationCommands().add("Write-Host 'Hello, World1!'");
		resourceCreationPlan.getCreationCommands().add("Write-Host 'Hello, World2!'");
		resourceCreationPlan.getCreationCommands().add("Write-Host 'Hello, World3!'");
		return resourceCreationPlan;
	}

	public void createResources(ResourceCreationPlan resourceCreationPlan) {
		for (String command : resourceCreationPlan.getCreationCommands()) {
			String output = powershellCaller.executeScript(command);
			System.out.println(output);
		}
	}
}
