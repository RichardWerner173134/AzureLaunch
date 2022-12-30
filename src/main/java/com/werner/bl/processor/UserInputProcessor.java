package com.werner.bl.processor;

import com.werner.bl.codegeneration.FunctionAppCodeGenerationManager;
import com.werner.bl.input.UserInputReader;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.validation.UserInputValidator;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@AllArgsConstructor
@Component
public class UserInputProcessor {

	@Autowired
	@Qualifier("configMap")
	protected Map<String, String> config;

	private final ResourceCreationManager resourceCreationManager;

	private final UserInputReader userInputReader;

	private final UserInputValidator userInputValidator;

	private final FunctionAppCodeGenerationManager functionAppCodeGenerationManager;

	public void process(String filePath) throws Exception {

		AzCodegenRequest parseFileRequest = userInputReader.readUserInput(new File(filePath));
		userInputValidator.validateUserInput(parseFileRequest);

		ResourceGraph resourceGraph = resourceCreationManager.computeResourceGraph(parseFileRequest);

		// create all non Functionapp related things
		ResourceCreationPlan resourceCreationPlan = resourceCreationManager.computeResourceCreationPlan(resourceGraph);
		//resourceCreationManager.createAzResources(resourceCreationPlan);

		// create all Functionapp related things
		functionAppCodeGenerationManager.generateAndDeployFunctionApps(resourceGraph, resourceCreationPlan);
	}
}
