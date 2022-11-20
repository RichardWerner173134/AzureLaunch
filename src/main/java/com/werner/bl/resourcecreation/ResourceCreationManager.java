package com.werner.bl.resourcecreation;

import com.werner.bl.input.generated.GraphEdges;
import com.werner.bl.input.generated.GraphNodes;
import com.werner.bl.input.generated.ParseFileRequest;
import com.werner.bl.resourcecreation.model.BasicResource;
import com.werner.bl.resourcecreation.model.CodegenResource;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.AbstractResourceNode;
import com.werner.helper.PowershellCaller;

import java.util.ArrayList;
import java.util.List;

public class ResourceCreationManager {
	private final PowershellCaller powershellCaller;

	public ResourceCreationManager() {
		powershellCaller = new PowershellCaller();
	}

	public ResourceGraph computeResourceGraph(ParseFileRequest request) {
		List<AbstractResourceNode> nodes = new ArrayList<>();
		List<ResourceEdge> edges = new ArrayList<>();

		for(GraphNodes node : request.getGraph().getNodes()) {
			String name = node.getName();
			String type = node.getType();
			if(type.equals("Microsoft.Web/sites/functions")) {
				nodes.add(new CodegenResource(name, type));
			} else {
				nodes.add(new BasicResource(name, type));
			}
		}

		for (GraphEdges edge : request.getGraph().getEdges()) {
			AbstractResourceNode node1  = null;
			AbstractResourceNode node2 = null;

			try {
				node1 = nodes.stream().filter(x -> x.getName().equals(edge.getNode1()))
						.findFirst().orElseThrow(() -> new Exception("node 1 not found in edges list"));
				node2 = nodes.stream().filter(x -> x.getName().equals(edge.getNode2()))
						.findFirst().orElseThrow(() -> new Exception("node 2 not found in edges list"));
			} catch (Exception e) { e.printStackTrace(); }

			edges.add(new ResourceEdge(node1, node2));
		}
		return new ResourceGraph(nodes, edges);
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
