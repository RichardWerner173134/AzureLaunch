package com.werner.bl.processor;

import com.werner.bl.input.UserInputReader;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.validation.UserInputValidator;
import generated.internal.v1_0_0.model.AzCodegenRequest;

import java.io.File;
import java.io.IOException;

public class UserInputProcessor {
	private final ResourceCreationManager resourceCreationManager;
	private final UserInputReader userInputReader;
	private final UserInputValidator userInputValidator;
	private final String filePath;

	public UserInputProcessor(String filePath) {
		this.userInputReader = new UserInputReader();
		this.userInputValidator = new UserInputValidator();
		this.resourceCreationManager = new ResourceCreationManager();
		this.filePath = filePath;
	}

	public void process() throws Exception {

		AzCodegenRequest parseFileRequest = userInputReader.readUserInput(new File(this.filePath));
		userInputValidator.validateUserInput(parseFileRequest);

		ResourceGraph resourceGraph = resourceCreationManager.computeResourceGraph(parseFileRequest);
		ResourceCreationPlan resourceCreationPlan = resourceCreationManager.computeResourceCreationPlan(resourceGraph);
		resourceCreationManager.createResources(resourceCreationPlan);
	}
}