package com.werner.bl.functionapp;

import com.werner.bl.functionapp.codegeneration.FunctionAppCodeGenerationResult;
import com.werner.bl.functionapp.codegeneration.FunctionAppCodeGenerator;
import com.werner.bl.functionapp.codegeneration.model.EdgeTypeMapper;
import com.werner.bl.functionapp.codegeneration.model.FunctionApp;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppClient;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppClientType;
import com.werner.bl.functionapp.codegeneration.model.enums.FunctionAppTriggerType;
import com.werner.bl.functionapp.deployment.FunctionAppDeploymentHandler;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

    private final EdgeTypeMapper edgeTypeMapper;

    private final FunctionAppCodeGenerator functionAppCodeGenerator;

    private final FunctionAppDeploymentHandler functionAppDeploymentHandler;

    public void generateAndDeployFunctionApps(ResourceGraph resourceGraph) throws Exception {
        // run through directed graph and collect triggers and clients per function
        List<FunctionApp> functionApps = computeFunctionAppConstruction(resourceGraph);


        List<FunctionAppCodeGenerationResult> results = new ArrayList<>();
        for (FunctionApp functionApp : functionApps) {
            FunctionAppCodeGenerationResult functionAppCodeGenerationResult = functionAppCodeGenerator.generateFunctionAppCode(functionApp);
            results.add(functionAppCodeGenerationResult);
        }

        // deploy every FunctionApp
        for (FunctionAppCodeGenerationResult result : results) {
            functionAppDeploymentHandler.deployFunctionAppCode(result.getResourceGroupName(), result.getFunctionName(), result.getArchivePath());
        }
    }

    private List<FunctionApp> computeFunctionAppConstruction(ResourceGraph resourceGraph){
        ArrayList<FunctionApp> functionApps = new ArrayList<>();

        for (ResourceEdge edge : resourceGraph.getEdges()) {
            if (edge.getResource1().getResourceType() == ResourceType.FUNCTION) {
                // both are functions
                String function1Name = edge.getResource1().getName();
                FunctionApp function1 = getOrCreateFunctionAppFromList(function1Name, functionApps);

                String function2Name = edge.getResource2().getName();
                FunctionApp function2 = getOrCreateFunctionAppFromList(function2Name, functionApps);

                FunctionAppClientType clientType = edgeTypeMapper.computeResultingClientType(edge);
                FunctionAppClient client = new FunctionAppClient("", clientType);
                function1.getClientList().add(client);

                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger("", triggerType);
                function2.getTriggerList().add(trigger);

            } else {
                // resource2 is a function

                String function2Name = edge.getResource2().getName();
                FunctionApp function2 = getOrCreateFunctionAppFromList(function2Name, functionApps);

                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger("", triggerType);
                function2.getTriggerList().add(trigger);
            }


            int x = 0;

        }

        return functionApps;
    }

    private FunctionApp getOrCreateFunctionAppFromList (String functionName, List<FunctionApp> functionApps) {
        Optional<FunctionApp> first = functionApps.stream()
                .filter(f -> f.getFunctionAppName().equals(functionName))
                .findFirst();

        if(first.isPresent()) {
            return first.get();
        } else {
            FunctionApp functionApp = new FunctionApp(functionName);
            functionApps.add(functionApp);
            return functionApp;
        }
    }
}
