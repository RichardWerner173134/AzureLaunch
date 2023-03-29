package com.werner.bl.processor;

import com.werner.bl.codegeneration.FunctionAppCodeGenerationManager;
import com.werner.bl.exception.*;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.input.UserInputReader;
import com.werner.input.UserInputValidator;
import com.werner.log.TaskLogger;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class UserInputProcessor {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserInputProcessor.class);

	private final ResourceCreationManager resourceCreationManager;

	private final UserInputReader userInputReader;

	private final UserInputValidator userInputValidator;

	private final FunctionAppCodeGenerationManager functionAppCodeGenerationManager;

	private final TaskLogger taskLogger;

	public void process(String filePath) {

		try {
			AzCodegenRequest parseFileRequest = userInputReader.readUserInput(filePath);
			userInputValidator.validateUserInput(parseFileRequest);

			ResourceGraph resourceGraph = resourceCreationManager.computeResourceGraph(parseFileRequest);

			// create all non Functionapp related things
			ResourceCreationPlan resourceCreationPlan = resourceCreationManager.computeResourceCreationPlan(resourceGraph);
			resourceCreationManager.createAzResources(resourceCreationPlan);

			// create all Functionapp related things
			functionAppCodeGenerationManager.generateAndDeployFunctionApps(resourceGraph, resourceCreationPlan,
					parseFileRequest.getAppConfig());

		} catch (InvalidInputFileContentException | InvalidInputFileException | ServicePrincipalException | AzureResourceCreationFailedException | NotImplementedException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			int c = 0;
		} finally {
			taskLogger.saveLogsToFile();
		}
	}
}
