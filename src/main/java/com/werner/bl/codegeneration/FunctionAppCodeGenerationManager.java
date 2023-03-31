package com.werner.bl.codegeneration;

import com.werner.bl.codegeneration.generators.projectlevel.Project;
import com.werner.bl.codegeneration.generators.projectlevel.ProjectGenerator;
import com.werner.bl.codegeneration.helper.EdgeHelper;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.exception.InvalidInputFileContentException;
import com.werner.bl.exception.NotImplementedException;
import com.werner.bl.localexecution.LocalFunctionExecutor;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.log.JavaTask;
import com.werner.log.TaskLogger;
import generated.internal.v1_0_0.model.AppConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

	private final static Logger LOG = LoggerFactory.getLogger(FunctionAppCodeGenerationManager.class);

	private final EdgeHelper edgeHelper;

	private final ProjectGenerator projectGenerator;

	private final LocalFunctionExecutor localFunctionExecutor;

	private final TaskLogger taskLogger;

	public void generateAndDeployFunctionApps(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan,
			AppConfig appConfig) throws IOException, InvalidInputFileContentException, NotImplementedException {
		// run through graph and collect triggers and clients per function
		List<FunctionApp> functionApps = computeFunctionAppConstruction(resourceGraph, resourceCreationPlan, appConfig);

		// generate code and projects zipped
		for (FunctionApp functionApp : functionApps) {
			taskLogger.beginNewTaskGroup("FunctionApp " + functionApp.getFunctionAppName());
			Project project = projectGenerator.generateProject(functionApp, resourceGraph.getServicePrincipal(), appConfig);

			if(appConfig.isLocalDeploymentOnly()) {
				int localPortNumber = functionApp.getLocalPortNumber();
				String targetFolderPath = project.getProjectRoot() + "\\target\\azure-functions\\" + functionApp.getFunctionAppName();
				localFunctionExecutor.startFunction(targetFolderPath, localPortNumber);

				String description = "\ncd " + targetFolderPath + "\nfunc start --port " + localPortNumber;
				String taskName = "Local FunctionApp Start";
				JavaTask javaTask = new JavaTask(description);
				taskLogger.addLogItem(javaTask, taskName);
			}
			taskLogger.endTaskGroup();
		}
	}

	private List<FunctionApp> computeFunctionAppConstruction(ResourceGraph resourceGraph,
															 ResourceCreationPlan resourceCreationPlan, AppConfig appConfig)
			throws InvalidInputFileContentException, NotImplementedException {
		FunctionApp.APP_SERVICE_PLAN_NAME = resourceGraph.getAppServicePlanName();
		FunctionApp.RESOURCE_GROUP_NAME = resourceGraph.getResourceGroup().getName();

		ArrayList<FunctionApp> functionApps = new ArrayList<>();

		for (ResourceEdge edge : resourceGraph.getEdges()) {
			handleEdge(edge, functionApps, resourceCreationPlan, appConfig);
		}

		return functionApps;
	}

	private void handleEdge(ResourceEdge edge, List<FunctionApp> functionApps,
			ResourceCreationPlan resourceCreationPlan, AppConfig appConfig)
			throws InvalidInputFileContentException, NotImplementedException {

		if (isPairOfFunctions(edge)) {
			edgeHelper.functionFunction(edge, functionApps, appConfig);
		} else if (isFunctionAndMessagingResource(edge)) {
			edgeHelper.functionNoFunction(edge, functionApps, resourceCreationPlan);
		} else {
			throw new InvalidInputFileContentException("Invalid InputFile. Cannot connect EdgeTypes "
					+ edge.getResource1().getResourceType().getName()
					+ " and "
					+ edge.getResource2().getResourceType().getName());
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
