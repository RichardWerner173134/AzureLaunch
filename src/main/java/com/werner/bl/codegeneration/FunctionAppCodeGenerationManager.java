package com.werner.bl.codegeneration;

import com.werner.bl.codegeneration.generators.projectlevel.ProjectGenerator;
import com.werner.bl.codegeneration.model.EdgeTypeMapper;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.codegeneration.model.enums.FunctionAppClientType;
import com.werner.bl.codegeneration.model.enums.FunctionAppTriggerType;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.deployment.Deployment;
import com.werner.bl.resourcecreation.model.graph.ResourceGraph;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.PowershellMavenAzFunCaller;
import com.werner.powershell.ServiceBusResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerationManager {

    private final ServiceBusResolver serviceBusResolver;

    private final EdgeTypeMapper edgeTypeMapper;

    private final ProjectGenerator projectGenerator;

    private final PowershellMavenAzFunCaller powershellMavenAzFunCaller;

    private static String RESOLVED_TEMP_DIR = null;

    public void generateAndDeployFunctionApps(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan) throws Exception {
        // run through directed graph and collect triggers and clients per function
        List<FunctionApp> functionApps = computeFunctionAppConstruction(resourceGraph, resourceCreationPlan);
        if (RESOLVED_TEMP_DIR == null) {
            RESOLVED_TEMP_DIR = powershellMavenAzFunCaller.getTempDir();
        }

        // generate code and projects zipped
        for (FunctionApp functionApp : functionApps) {
            projectGenerator.generateProject(functionApp, RESOLVED_TEMP_DIR);
        }
    }

    // TODO refactoring
    private List<FunctionApp> computeFunctionAppConstruction(ResourceGraph resourceGraph, ResourceCreationPlan resourceCreationPlan) {
        ArrayList<FunctionApp> functionApps = new ArrayList<>();

        for (ResourceEdge edge : resourceGraph.getEdges()) {
            if (edge.getResource1().getResourceType() == ResourceType.FUNCTION_APP) {
                // create FunctionApp objects
                String functionApp1Name = edge.getResource1().getName();
                FunctionApp functionApp1 = getOrCreateFunctionAppFromList(functionApp1Name, functionApps);
                String functionApp2Name = edge.getResource2().getName();
                FunctionApp functionApp2 = getOrCreateFunctionAppFromList(functionApp2Name, functionApps);

                // assign client and trigger
                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger(triggerType);
                functionApp2.addTrigger(trigger);

                FunctionAppClientType clientType = edgeTypeMapper.computeResultingClientType(edge);
                FunctionAppClient client = new FunctionAppClient(clientType);
                // functionApp2 and functionApp2Trigger are used to define url endpoint later
                Map<String, String> configurationMap = computeConfigurationMapForClient(functionApp2.getFunctionAppName(), trigger.getTriggerName());
                client.getClientParams().putAll(configurationMap);
                functionApp1.addClient(client);
            } else {
                String functionApp2Name = edge.getResource2().getName();
                FunctionApp functionApp2 = getOrCreateFunctionAppFromList(functionApp2Name, functionApps);

                FunctionAppTriggerType triggerType = edgeTypeMapper.computeResultingTriggerType(edge);
                FunctionAppTrigger trigger = new FunctionAppTrigger(triggerType);
                Deployment deployment = getDeploymentCompositeForResource(edge.getResource1(), resourceCreationPlan);
                Map<String, String> configurationMap = computeConfigurationMapForTrigger(deployment, triggerType);
                trigger.getTriggerParams().putAll(configurationMap);
                functionApp2.addTrigger(trigger);

                // TODO delegate which configs need to be resolved
                String value = serviceBusResolver.resolveServiceBusConnection();
                functionApp2.getAdditionalProperties().put("ServiceBusConnectionString", value);
            }
        }

        FunctionApp.APP_SERVICE_PLAN_NAME = resourceGraph.getAppServicePlanName();
        FunctionApp.RESOURCE_GROUP_NAME = resourceGraph.getResourceGroup().getName();

        return functionApps;
    }


    private FunctionApp getOrCreateFunctionAppFromList(String functionName, List<FunctionApp> functionApps) {
        String functionAppName = functionName;
        Optional<FunctionApp> first = functionApps.stream()
                .filter(f -> f.getFunctionAppName().equals(functionAppName))
                .findFirst();

        if (first.isPresent()) {
            return first.get();
        } else {
            FunctionApp functionApp = new FunctionApp(functionAppName);
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
        switch (triggerType) {
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

    private Map<String, String> computeConfigurationMapForClient(String targetFunctionAppName, String targetFunctionAppTriggerName) {
        Map<String, String> map = new HashMap<>();
        map.put("url", "https://" + targetFunctionAppName + ".azurewebsites.net/api/" + targetFunctionAppTriggerName);
        return map;
    }
}
