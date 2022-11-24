package com.werner.bl.resourcecreation;

import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.dependency.Dependency;
import com.werner.bl.resourcecreation.model.dependency.DependencyHierarchy;
import com.werner.bl.resourcecreation.model.deployment.Deployment;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceNodeFactory;
import com.werner.helper.PowershellCaller;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import generated.internal.v1_0_0.model.GraphEdges;
import generated.internal.v1_0_0.model.GraphNodes;

import java.util.ArrayList;
import java.util.List;

public class ResourceCreationManager {
	private final PowershellCaller powershellCaller;
	private final DependencyHierarchy dependencyHierarchy;
	private final ResourceNodeFactory resourceNodeFactory;

	public ResourceCreationManager() {
		powershellCaller = new PowershellCaller();
		dependencyHierarchy = new DependencyHierarchy();
		resourceNodeFactory = new ResourceNodeFactory();
	}

	public ResourceGraph computeResourceGraph(AzCodegenRequest request) {
		List<AbstractResourceNode> nodes = new ArrayList<>();
		List<ResourceEdge> edges = new ArrayList<>();

		for(GraphNodes node : request.getGraph().getNodes()) {
			String name = node.getName();
			ResourceType type = ResourceType.findById(node.getType());

			AbstractResourceNode newNode = resourceNodeFactory.create(name, type);
			nodes.add(newNode);
		}

		for (GraphEdges edge : request.getGraph().getEdges()) {
			AbstractResourceNode node1  = null;
			AbstractResourceNode node2 = null;

			try {
				node1 = nodes.stream().filter(x -> x.getName().equals(edge.getNode1()))
						.findFirst().orElseThrow(() -> new Exception("node 1 not found in edges list: " + edge));
				node2 = nodes.stream().filter(x -> x.getName().equals(edge.getNode2()))
						.findFirst().orElseThrow(() -> new Exception("node 2 not found in edges list" + edge));
			} catch (Exception e) {
				e.printStackTrace();
			}

			edges.add(new ResourceEdge(node1, node2));
		}
		return new ResourceGraph(nodes, edges);
	}

	public ResourceCreationPlan computeResourceCreationPlan(ResourceGraph resourceGraph) {
		List<Deployment> deployments = new ArrayList<>();
		ResourceCreationPlan resourceCreationPlan = new ResourceCreationPlan();
		resourceCreationPlan.setDeployments(deployments);

		// add dependencies to ResourceGraph
		for (AbstractResourceNode node : resourceGraph.getNodes()) {
			Deployment deployment = new Deployment();
			deployment.getResourceFamily().add(node);
			addParentResources(node, deployment);
			deployments.add(deployment);
		}

		return resourceCreationPlan;
	}

	public void createResources(ResourceCreationPlan resourceCreationPlan) throws Exception {
		for (Deployment deployment : resourceCreationPlan.getDeployments()) {
			deployment.deploy();
		}
	}

	public void addParentResources(AbstractResourceNode node, Deployment deployment){
		Dependency dependency = dependencyHierarchy.findByType(node.getResourceType());

		if(dependency != null) {
			String name = dependency.getDependencyType().getShortName() + "for" + node.getName();
			AbstractResourceNode parentNode = resourceNodeFactory.create(name, dependency.getDependencyType());
			deployment.getResourceFamily().add(parentNode);
			addParentResources(parentNode, deployment);
		}
	}
}
