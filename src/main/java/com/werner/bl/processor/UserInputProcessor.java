package com.werner.bl.processor;

import com.werner.bl.functionapp.FunctionAppCodeGenerationManager;
import com.werner.bl.input.UserInputReader;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.validation.UserInputValidator;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@AllArgsConstructor
@Component
public class UserInputProcessor {
	private final ResourceCreationManager resourceCreationManager;

	private final UserInputReader userInputReader;

	private final UserInputValidator userInputValidator;

	private final FunctionAppCodeGenerationManager functionAppCodeGenerationManager;

	public void process(String filePath) throws Exception {

		AzCodegenRequest parseFileRequest = userInputReader.readUserInput(new File(filePath));
		userInputValidator.validateUserInput(parseFileRequest);

		ResourceGraph resourceGraph = resourceCreationManager.computeResourceGraph(parseFileRequest);
		ResourceCreationPlan resourceCreationPlan = resourceCreationManager.computeResourceCreationPlan(resourceGraph);
		resourceCreationManager.createAzResources(resourceCreationPlan);

		functionAppCodeGenerationManager.generateAndDeployFunctionApps(resourceGraph, resourceCreationPlan);
	}
}
