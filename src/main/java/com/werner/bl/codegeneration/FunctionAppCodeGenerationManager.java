package com.werner.bl.codegeneration;

import com.werner.bl.codegeneration.generators.projectlevel.Project;
import com.werner.bl.codegeneration.generators.projectlevel.ProjectGenerator;
import com.werner.bl.codegeneration.helper.EdgeHelper;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.localexecution.LocalFunctionExecutor;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import generated.internal.v1_0_0.model.AppConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

	private final static Logger LOG = LoggerFactory.getLogger(FunctionAppCodeGenerationManager.class);

	private final EdgeHelper edgeHelper;

	private final ProjectGenerator projectGenerator;

	private final LocalFunctionExecutor localFunctionExecutor;

	public void generateAndDeployFunctionApps(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan,
			AppConfig appConfig) {
		// run through directed graph and collect triggers and clients per function
		List<FunctionApp> functionApps = computeFunctionAppConstruction(resourceGraph, resourceCreationPlan, appConfig);

		List<String> logMessages = new ArrayList<>();
		logMessages.add("Copy following commands to start functions locally:");

		// generate code and projects zipped
		for (FunctionApp functionApp : functionApps) {
			Project project = projectGenerator.generateProject(functionApp, appConfig);

			if(appConfig.isLocalDeploymentOnly()) {
				int localPortNumber = functionApp.getLocalPortNumber();
				String targetFolderPath = project.getProjectRoot() + "\\target\\azure-functions\\" + functionApp.getFunctionAppName();
				localFunctionExecutor.startFunction(targetFolderPath, localPortNumber);
				logMessages.add("\n\n" + functionApp.getFunctionAppName() + ":\ncd " + targetFolderPath + "\nfunc start --port " + localPortNumber);
			}
		}

		for (String m : logMessages) {
			LOG.info(m);
		}
	}

	private List<FunctionApp> computeFunctionAppConstruction(ResourceGraph resourceGraph,
															 ResourceCreationPlan resourceCreationPlan, AppConfig appConfig) {
		FunctionApp.APP_SERVICE_PLAN_NAME = resourceGraph.getAppServicePlanName();
		FunctionApp.RESOURCE_GROUP_NAME = resourceGraph.getResourceGroup().getName();

		ArrayList<FunctionApp> functionApps = new ArrayList<>();

		for (ResourceEdge edge : resourceGraph.getEdges()) {
			handleEdge(edge, functionApps, resourceCreationPlan, appConfig);
		}

		return functionApps;
	}

	private void handleEdge(ResourceEdge edge, List<FunctionApp> functionApps,
			ResourceCreationPlan resourceCreationPlan, AppConfig appConfig) {

		if (isPairOfFunctions(edge)) {
			edgeHelper.functionFunction(edge, functionApps, appConfig);
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
