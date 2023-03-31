package com.werner.bl.resourcecreation;

import com.werner.bl.exception.AzureResourceCreationFailedException;
import com.werner.bl.exception.InvalidInputFileContentException;
import com.werner.bl.exception.ServicePrincipalException;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.dependency.Dependency;
import com.werner.bl.resourcecreation.model.dependency.DependencyHierarchy;
import com.werner.bl.resourcecreation.model.deployment.Deployment;
import com.werner.bl.resourcecreation.model.deployment.DeploymentHandler;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.*;
import com.werner.log.TaskLogger;
import com.werner.powershell.ServicePrincipalResolver;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import generated.internal.v1_0_0.model.GraphEdges;
import generated.internal.v1_0_0.model.GraphNodes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ResourceCreationManager {

	private final ServicePrincipalResolver servicePrincipalResolver;

	private final DependencyHierarchy dependencyHierarchy;

	private final ResourceNodeFactory resourceNodeFactory;

	private final DeploymentHandler deploymentHandler;

	private final TaskLogger taskLogger;

	public ResourceGraph computeResourceGraph(AzCodegenRequest request)
			throws ServicePrincipalException, InvalidInputFileContentException {
		// resourceGroup
		ResourceGroup resourceGroup = new ResourceGroup(
				request.getAzAccount().getResourceGroupName(),
				request.getAzAccount().getResourceGroupLocation());

		// one appServicePlan for all functions
		String appServicePlanName = request.getAzAccount().getAppServicePlanName();

		// ServicePrincipal
		String servicePrincipalName = request.getAzAccount().getServicePrincipalName();

		ServicePrincipal servicePrincipal = servicePrincipalResolver.getOrCreateServicePrincipal(
				servicePrincipalName);

		List<AbstractResourceNode> nodes = new ArrayList<>();
		List<ResourceEdge> edges = new ArrayList<>();

		for(GraphNodes node : request.getGraph().getNodes()) {
			String name = node.getName();
			ResourceType type = ResourceType.findById(node.getType());

			AbstractResourceNode newNode = resourceNodeFactory.create(name, type);
			nodes.add(newNode);
		}

		for (GraphEdges edge : request.getGraph().getEdges()) {

			AbstractResourceNode node1 = nodes.stream().filter(x -> x.getName().equals(edge.getNode1())).findFirst().get();
			AbstractResourceNode node2 = nodes.stream().filter(x -> x.getName().equals(edge.getNode2())).findFirst().get();

			String type = edge.getType();
			EdgeType edgeType = EdgeType.findById(type);

			edges.add(new ResourceEdge(node1, node2, edgeType));
		}
		return new ResourceGraph(resourceGroup, appServicePlanName, servicePrincipal, nodes, edges);
	}

	public ResourceCreationPlan computeResourceCreationPlan(ResourceGraph resourceGraph) {
		ResourceCreationPlan resourceCreationPlan = new ResourceCreationPlan();
		List<Deployment> deployments = new ArrayList<>();
		resourceCreationPlan.setDeployments(deployments);

		ResourceGroup resourceGroup = resourceGraph.getResourceGroup();
		Deployment rgDeployment = new Deployment();
		rgDeployment.getDeploymentComposite().add(resourceGroup);

		deployments.add(rgDeployment);

		// add dependencies to ResourceGraph
		for (AbstractResourceNode node : resourceGraph.getNodes()) {

			if(node.getResourceType() == ResourceType.FUNCTION_APP) {
				continue;
			}

			Deployment deployment = new Deployment();
			deployment.getDeploymentComposite().add(node);
			addParentResources(node, deployment);
			deployments.add(deployment);
		}

		return resourceCreationPlan;
	}

	public void createAzResources(ResourceCreationPlan resourceCreationPlan)
			throws AzureResourceCreationFailedException, IOException {
		taskLogger.beginNewTaskGroup("Using ARM-Templates to create Non-Compute Resources");
		for (Deployment deployment : resourceCreationPlan.getDeployments()) {
			deploymentHandler.deploy(deployment);
		}
		taskLogger.endTaskGroup();
	}

	private void addParentResources(AbstractResourceNode node, Deployment deployment){
		Dependency dependency = dependencyHierarchy.findByType(node.getResourceType());

		if(dependency != null) {
			String name = dependency.getDependencyType().getShortName() + "for" + node.getName();
			AbstractResourceNode parentNode = resourceNodeFactory.create(name, dependency.getDependencyType());
			deployment.getDeploymentComposite().add(parentNode);
			addParentResources(parentNode, deployment);
		}
	}
}
