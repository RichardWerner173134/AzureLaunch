package com.werner.bl.codegeneration;

import com.werner.bl.codegeneration.generators.projectlevel.ProjectGenerator;
import com.werner.bl.codegeneration.helper.EdgeHelper;
import com.werner.bl.codegeneration.helper.EdgeTypeMapper;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.PowershellMavenAzFunCaller;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

	private static String RESOLVED_TEMP_DIR = null;

	private final EdgeTypeMapper edgeTypeMapper;

	private final EdgeHelper edgeHelper;

	private final ProjectGenerator projectGenerator;

	private final PowershellMavenAzFunCaller powershellMavenAzFunCaller;

	public void generateAndDeployFunctionApps(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan)
			throws Exception {
		// run through directed graph and collect triggers and clients per function
		List<FunctionApp> functionApps = computeFunctionAppConstruction2(resourceGraph, resourceCreationPlan);
		if (RESOLVED_TEMP_DIR == null) {
			RESOLVED_TEMP_DIR = powershellMavenAzFunCaller.getTempDir();
		}

		// generate code and projects zipped
		for (FunctionApp functionApp : functionApps) {
			projectGenerator.generateProject(functionApp, RESOLVED_TEMP_DIR);
		}
	}

	private List<FunctionApp> computeFunctionAppConstruction2(ResourceGraph resourceGraph,
			ResourceCreationPlan resourceCreationPlan) {
		FunctionApp.APP_SERVICE_PLAN_NAME = resourceGraph.getAppServicePlanName();
		FunctionApp.RESOURCE_GROUP_NAME = resourceGraph.getResourceGroup().getName();

		ArrayList<FunctionApp> functionApps = new ArrayList<>();

		for (ResourceEdge edge : resourceGraph.getEdges()) {
			handleEdge(edge, functionApps, resourceCreationPlan);
		}

		return functionApps;
	}

	private void handleEdge(ResourceEdge edge, List<FunctionApp> functionApps,
			ResourceCreationPlan resourceCreationPlan) {
		AbstractResourceNode resource1 = edge.getResource1();
		AbstractResourceNode resource2 = edge.getResource2();

		if (isPairOfFunctions(edge)) {
			edgeHelper.functionFunction(edge, functionApps, resourceCreationPlan);
		} else if (isFunctionAndMessagingResource(edge)) {
			edgeHelper.functionNoFunction(edge, functionApps, resourceCreationPlan);
		} else {
			throw new RuntimeException("Resourcetype combination impossible in edge ");
		}
	}

	private boolean isPairOfFunctions(ResourceEdge edge) {
		return edge.getResource1().getResourceType() == ResourceType.FUNCTION_APP
				&& edge.getResource2().getResourceType() == ResourceType.FUNCTION_APP;
	}

	private boolean isFunctionAndMessagingResource(ResourceEdge edge) {
		return edge.getResource1().getResourceType() == ResourceType.FUNCTION_APP
				&& edge.getResource2().getResourceType() != ResourceType.FUNCTION_APP
				|| edge.getResource1().getResourceType() != ResourceType.FUNCTION_APP
				&& edge.getResource2().getResourceType() == ResourceType.FUNCTION_APP;
	}
}
