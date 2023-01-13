package com.werner.bl.processor;


import com.werner.bl.codegeneration.FunctionAppCodeGenerationManager;
import com.werner.bl.input.UserInputReader;
import com.werner.bl.resourcecreation.ResourceCreationManager;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.validation.UserInputValidator;
import generated.internal.v1_0_0.model.AppConfig;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class UserInputProcessorTest {

    @Mock
    private AppConfig appConfig;

    @Mock
    private UserInputReader userInputReader;

    @Mock
    private UserInputValidator userInputValidator;

    @Mock
    private ResourceCreationManager resourceCreationManager;

    @Mock
    private FunctionAppCodeGenerationManager functionAppCodeGenerationManager;

    @Mock
    private AzCodegenRequest parseFileRequest;

    @Mock
    private ResourceGraph resourceGraph;

    @Mock
    private ResourceCreationPlan resourceCreationPlan;

    @InjectMocks
    private UserInputProcessor userInputProcessor;


    @Test
    public void processTest() throws Exception {
        String filePath = "filePath";


        // mock the readUserInput, validateUserInput, computeResourceGraph, computeResourceCreationPlan, generateAndDeployFunctionApps methods
        Mockito.when(parseFileRequest.getAppConfig()).thenReturn(appConfig);
        Mockito.when(userInputReader.readUserInput(Mockito.any(File.class))).thenReturn(parseFileRequest);
        Mockito.doNothing().when(userInputValidator).validateUserInput(Mockito.any(AzCodegenRequest.class));
        Mockito.when(resourceCreationManager.computeResourceGraph(Mockito.any(AzCodegenRequest.class))).thenReturn(resourceGraph);
        Mockito.when(resourceCreationManager.computeResourceCreationPlan(Mockito.any(ResourceGraph.class))).thenReturn(resourceCreationPlan);
        Mockito.doNothing().when(functionAppCodeGenerationManager).generateAndDeployFunctionApps(Mockito.any(ResourceGraph.class), Mockito.any(ResourceCreationPlan.class), Mockito.any(AppConfig.class));

        userInputProcessor.process(filePath);
        // verify the calls to readUserInput, validateUserInput, computeResourceGraph, computeResourceCreationPlan, generateAndDeployFunctionApps methods
        Mockito.verify(userInputReader, Mockito.times(1)).readUserInput(ArgumentMatchers.any(File.class));
        Mockito.verify(userInputValidator, Mockito.times(1)).validateUserInput(ArgumentMatchers.any(AzCodegenRequest.class));
        Mockito.verify(resourceCreationManager, Mockito.times(1)).computeResourceGraph(ArgumentMatchers.any(AzCodegenRequest.class));
        Mockito.verify(resourceCreationManager, Mockito.times(1)).createAzResources(ArgumentMatchers.any(ResourceCreationPlan.class));
        Mockito.verify(functionAppCodeGenerationManager, Mockito.times(1)).generateAndDeployFunctionApps(
                ArgumentMatchers.any(ResourceGraph.class),
                ArgumentMatchers.any(ResourceCreationPlan.class),
                ArgumentMatchers.any(AppConfig.class));

    }
}