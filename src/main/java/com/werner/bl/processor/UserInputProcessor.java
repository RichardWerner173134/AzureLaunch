package com.werner.bl.processor;

import com.werner.bl.codegeneration.FunctionAppCodeGenerationManager;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.input.UserInputReader;
import com.werner.input.UserInputValidator;
import com.werner.log.PowershellTaskLogger;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserInputProcessor {

	private final ResourceCreationManager resourceCreationManager;

	private final UserInputReader userInputReader;

	private final UserInputValidator userInputValidator;

	private final FunctionAppCodeGenerationManager functionAppCodeGenerationManager;

	private final PowershellTaskLogger powershellTaskLogger;

	public void process(String filePath) {

		AzCodegenRequest parseFileRequest = userInputReader.readUserInput(filePath);
		userInputValidator.validateUserInput(parseFileRequest);

		ResourceGraph resourceGraph = resourceCreationManager.computeResourceGraph(parseFileRequest);

		// create all non Functionapp related things
		ResourceCreationPlan resourceCreationPlan = resourceCreationManager.computeResourceCreationPlan(resourceGraph);
		resourceCreationManager.createAzResources(resourceCreationPlan);

		// create all Functionapp related things
		functionAppCodeGenerationManager.generateAndDeployFunctionApps(resourceGraph, resourceCreationPlan, parseFileRequest.getAppConfig());

		powershellTaskLogger.saveLogsToFile();
	}
}
