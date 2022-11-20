package com.werner.bl.resourcecreation;

import com.werner.bl.resourcecreation.model.graph.node.BasicResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.CodegenResourceNode;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.dependency.DependencyHierarchy;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.helper.PowershellCaller;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import generated.internal.v1_0_0.model.GraphEdges;
import generated.internal.v1_0_0.model.GraphNodes;

import java.util.ArrayList;
import java.util.List;

public class ResourceCreationManager {
	private final PowershellCaller powershellCaller;
	private final DependencyHierarchy dependencyHierarchy;

	public ResourceCreationManager() {
		powershellCaller = new PowershellCaller();
		dependencyHierarchy = new DependencyHierarchy();
	}

	public ResourceGraph computeResourceGraph(AzCodegenRequest request) {
		List<AbstractResourceNode> nodes = new ArrayList<>();
		List<ResourceEdge> edges = new ArrayList<>();

		for(GraphNodes node : request.getGraph().getNodes()) {
			String name = node.getName();
			ResourceType type = ResourceType.findById(node.getType());

			if(type == ResourceType.FUNCTION || type == ResourceType.FUNCTION_APP) {
				nodes.add(new CodegenResourceNode(name, type));
			} else {
				nodes.add(new BasicResourceNode(name, type));
			}
		}

		for (GraphEdges edge : request.getGraph().getEdges()) {
			AbstractResourceNode node1  = null;
			AbstractResourceNode node2 = null;

			try {
				node1 = nodes.stream().filter(x -> x.getName().equals(edge.getNode1()))
						.findFirst().orElseThrow(() -> new Exception("node 1 not found in edges list: " + edge));
				node2 = nodes.stream().filter(x -> x.getName().equals(edge.getNode2()))
						.findFirst().orElseThrow(() -> new Exception("node 2 not found in edges list" + edge));
			} catch (Exception e) { e.printStackTrace(); }

			edges.add(new ResourceEdge(node1, node2));
		}
		return new ResourceGraph(nodes, edges);
	}

	public ResourceCreationPlan computeResourceCreationPlan(ResourceGraph resourceGraph) {
		ResourceCreationPlan resourceCreationPlan = new ResourceCreationPlan();

		// use dependencyHierarchy to create ResourceCreationPlan
		resourceGraph.get
		return resourceCreationPlan;
	}

	public void createResources(ResourceCreationPlan resourceCreationPlan) {
		for (String command : resourceCreationPlan.getCreationCommands()) {
			String output = powershellCaller.executeScript(command);
			System.out.println(output);
		}
	}
}
