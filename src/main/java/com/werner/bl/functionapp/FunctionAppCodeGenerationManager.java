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
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.deployment.Deployment;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

    private final EdgeTypeMapper edgeTypeMapper;

    private final FunctionAppCodeGenerator functionAppCodeGenerator;

    private final FunctionAppDeploymentHandler functionAppDeploymentHandler;

    public void generateAndDeployFunctionApps(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan) throws Exception {
        // run through directed graph and collect triggers and clients per function
        List<FunctionApp> functionApps = computeFunctionAppConstruction(resourceGraph, resourceCreationPlan);

        // generate code and projects zipped
        List<FunctionAppCodeGenerationResult> results = new ArrayList<>();
        for (FunctionApp functionApp : functionApps) {
            FunctionAppCodeGenerationResult functionAppCodeGenerationResult = functionAppCodeGenerator.generateFunctionAppProject(functionApp, resourceGraph.getResourceGroup());
            results.add(functionAppCodeGenerationResult);
        }

        // deploy every FunctionApp
        for (FunctionAppCodeGenerationResult result : results) {
            functionAppDeploymentHandler.deployFunctionAppCode(result.getResourceGroupName(), result.getFunctionName(), result.getArchivePath());
        }
    }

    private List<FunctionApp> computeFunctionAppConstruction(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan){
        ArrayList<FunctionApp> functionApps = new ArrayList<>();

        for (ResourceEdge edge : resourceGraph.getEdges()) {
            if (edge.getResource1().getResourceType() == ResourceType.FUNCTION) {
                String function1Name = edge.getResource1().getName();
                FunctionApp function1 = getOrCreateFunctionAppFromList(function1Name, functionApps);

                String function2Name = edge.getResource2().getName();
                FunctionApp function2 = getOrCreateFunctionAppFromList(function2Name, functionApps);

                FunctionAppClientType clientType = edgeTypeMapper.computeResultingClientType(edge);
                FunctionAppClient client = new FunctionAppClient(edge.getResource1().getName() + "-"
                        + clientType.getName() + "-client", clientType);
                //computeConfigurationMapForClient();

                function1.getClientList().add(client);

                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger(edge.getResource2().getName() + "-"
                        + clientType.getName() + "-trigger", triggerType);
                function2.getTriggerList().add(trigger);

            } else {
                String function2Name = edge.getResource2().getName();
                FunctionApp function2 = getOrCreateFunctionAppFromList(function2Name, functionApps);

                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger(edge.getResource2().getName() + "-"
                        + triggerType.getName() + "-trigger", triggerType);
                Deployment deployment = getDeploymentCompositeForResource(edge.getResource1(), resourceCreationPlan);
                Map<String, String> configurationMap = computeConfigurationMapForTrigger(deployment, triggerType);
                trigger.getTriggerParams().putAll(configurationMap);
                function2.getTriggerList().add(trigger);
            }
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

    private Deployment getDeploymentCompositeForResource(AbstractResourceNode resourceNode, ResourceCreationPlan resourceCreationPlan) {
        String name = resourceNode.getName();
        for (Deployment deployment : resourceCreationPlan.getDeployments()) {
            Optional<AbstractResourceNode> first = deployment.getDeploymentComposite().stream()
                    .filter(x -> x.getName().equals(name))
                    .findFirst();

            if (first.isPresent()) {
                return deployment;
            }
        }
        throw new IllegalStateException();
    }

    private Map<String, String> computeConfigurationMapForTrigger(Deployment deployment, FunctionAppTriggerType triggerType) {
        HashMap<String, String> map = new HashMap<>();
        switch(triggerType) {
            case SERVICE_BUS_PUB_SUB:
                map.put(ResourceType.SERVICEBUS_TOPIC.getName(), deployment.getDeploymentComposite().stream()
                        .filter(d -> d.getResourceType() == ResourceType.SERVICEBUS_TOPIC).findFirst().get().getName());
                map.put(ResourceType.SERVICEBUS_SUBSCRIPTION.getName(), deployment.getDeploymentComposite().stream()
                        .filter(d -> d.getResourceType() == ResourceType.SERVICEBUS_SUBSCRIPTION).findFirst().get().getName());
                break;
            case SERVICE_BUS_QUEUE:
                break;
            case HTTP_GET:
                break;
            case HTTP_POST:
                break;
        }

        return map;
    }

    private Map<String, String> computeConfigurationMapForClient(String targetFunctionName, FunctionAppClientType clientType) {
        HashMap<String, String> map = new HashMap<>();
        switch(clientType) {
            case HTTP_GET:
                map.put("url", "https://" + ".azurewebsites.net/api/" + );
                break;
            case HTTP_POST:
                break;
        }

        return map;
    }
}
