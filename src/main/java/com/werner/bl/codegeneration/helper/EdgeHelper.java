package com.werner.bl.codegeneration.helper;

import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.codegeneration.model.enums.ClientParam;
import com.werner.bl.codegeneration.model.enums.FunctionAppClientType;
import com.werner.bl.codegeneration.model.enums.FunctionAppTriggerType;
import com.werner.bl.codegeneration.model.enums.TriggerParam;
import com.werner.bl.exception.InvalidInputFileContentException;
import com.werner.bl.exception.NotImplementedException;
import com.werner.bl.resourcecreation.model.ResourceCreationPlan;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.deployment.Deployment;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.BasicResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.CodegenResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.EdgeType;
import com.werner.powershell.ServiceBusResolver;
import generated.internal.v1_0_0.model.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EdgeHelper {

    private final ServiceBusResolver serviceBusResolver;

    private int nextPortNumber = 8080;

    public void functionFunction(ResourceEdge edge, List<FunctionApp> functionApps,
                                 AppConfig appConfig) throws InvalidInputFileContentException {
        FunctionApp fa1 = getOrCreateFunctionAppFromList((CodegenResourceNode) edge.getResource1(), functionApps);
        FunctionApp fa2 = getOrCreateFunctionAppFromList((CodegenResourceNode) edge.getResource2(), functionApps);

        EdgeType edgeType = edge.getEdgeType();

        FunctionAppClient client;
        FunctionAppTrigger trigger;

        switch (edgeType) {
            case HTTP_GET:
                client = new FunctionAppClient(FunctionAppClientType.HTTP_GET);
                trigger = new FunctionAppTrigger(FunctionAppTriggerType.HTTP_GET);
                break;
            case HTTP_POST:
                client = new FunctionAppClient(FunctionAppClientType.HTTP_POST);
                trigger = new FunctionAppTrigger(FunctionAppTriggerType.HTTP_POST);

                client.getClientParams().put(ClientParam.P_JSON_BODY.getValue(), "{\\\"message\\\":\\\"hi\\\"}");
                break;
            default:
                throw new InvalidInputFileContentException("Invalid Inputfile. Two functions have to have the edge type "
                        + EdgeType.HTTP_GET.getName() + " or " + EdgeType.HTTP_POST.getName() + ". "
                        + edge.getResource1() + " and " + edge.getResource2()
                        + " have EdgeType " + edge.getEdgeType().getName());
        }

        fa1.addClient(client);
        fa2.addTrigger(trigger);

        String url = appConfig.isLocalDeploymentOnly() == true
                ? "http://localhost:" + fa2.getLocalPortNumber() + "/api/" + trigger.getTriggerName()
                : "https://" + fa2.getFunctionAppName() + ".azurewebsites.net/api/" + trigger.getTriggerName();

        fa1.addAppSettingToClient(client, ClientParam.AS_TARGET_URL.getValue(), url);
    }

    public void functionNoFunction(ResourceEdge edge, List<FunctionApp> functionApps,
                                   ResourceCreationPlan resourceCreationPlan) throws NotImplementedException {

        BasicResourceNode nonFunctionResource;
        FunctionApp functionApp;

        if (edge.getResource1() instanceof BasicResourceNode) {
            nonFunctionResource = (BasicResourceNode) edge.getResource1();
            functionApp = getOrCreateFunctionAppFromList((CodegenResourceNode) edge.getResource2(), functionApps);
        } else {
            nonFunctionResource = (BasicResourceNode) edge.getResource2();
            functionApp = getOrCreateFunctionAppFromList((CodegenResourceNode) edge.getResource2(), functionApps);
        }

        EdgeType edgeType = edge.getEdgeType();

        Deployment d = getDeploymentCompositeForResource(nonFunctionResource,
                resourceCreationPlan);

        addCodeConstructionUnitsToFunction(nonFunctionResource, functionApp, d, edgeType);
    }

    private void addCodeConstructionUnitsToFunction(BasicResourceNode basicResourceNode, FunctionApp functionApp,
                                                    Deployment basicResourceDeployment, EdgeType edgeType)
            throws NotImplementedException {

        ResourceType resourceType = basicResourceNode.getResourceType();

        FunctionAppTrigger trigger;

        if (resourceType == ResourceType.SERVICEBUS_QUEUE && edgeType == EdgeType.READ) {
            trigger = new FunctionAppTrigger(FunctionAppTriggerType.SERVICE_BUS_QUEUE);
            functionApp.addTrigger(trigger);

            String sbns = basicResourceDeployment.getDeploymentComposite().stream()
                    .filter(dc -> dc.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
            String sbQueue = basicResourceDeployment.getDeploymentComposite().stream()
                    .filter(dc -> dc.getResourceType() == ResourceType.SERVICEBUS_QUEUE).findFirst().get().getName();

            String connectionString = serviceBusResolver.resolveServiceBusConnectionString(FunctionApp.RESOURCE_GROUP_NAME, sbns);
            functionApp.addAppSettingToTrigger(trigger, TriggerParam.AS_CONNECTIONSTRING.getValue(), connectionString);

            trigger.getTriggerParams().put(TriggerParam.P_SERVICEBUS_QUEUE_NAME.getValue(), sbQueue);
        } else if (resourceType == ResourceType.SERVICEBUS_QUEUE && edgeType == EdgeType.WRITE) {
            throw new NotImplementedException("Servicebus binding not implemented yet");
        } else if (resourceType == ResourceType.SERVICEBUS_SUBSCRIPTION && edgeType == EdgeType.READ) {
            trigger = new FunctionAppTrigger(FunctionAppTriggerType.SERVICE_BUS_PUB_SUB);
            functionApp.addTrigger(trigger);

            String sbns = basicResourceDeployment.getDeploymentComposite().stream()
                    .filter(dc -> dc.getResourceType() == ResourceType.SERVICEBUS_NAMESPACE).findFirst().get().getName();
            String sbTopic = basicResourceDeployment.getDeploymentComposite().stream()
                    .filter(dc -> dc.getResourceType() == ResourceType.SERVICEBUS_TOPIC).findFirst().get().getName();
            String sbSubscription = basicResourceDeployment.getDeploymentComposite().stream()
                    .filter(dc -> dc.getResourceType() == ResourceType.SERVICEBUS_SUBSCRIPTION).findFirst().get().getName();

            String connectionString = serviceBusResolver.resolveServiceBusConnectionString(FunctionApp.RESOURCE_GROUP_NAME, sbns);
            functionApp.addAppSettingToTrigger(trigger, TriggerParam.AS_CONNECTIONSTRING.getValue(), connectionString);

            trigger.getTriggerParams().put(TriggerParam.P_SERVICEBUS_TOPIC_NAME.getValue(), sbTopic);
            trigger.getTriggerParams().put(TriggerParam.P_SERVICEBUS_SUBSCRIPTION_NAME.getValue(), sbSubscription);
        } else if (resourceType == ResourceType.SERVICEBUS_TOPIC && edgeType == EdgeType.WRITE) {
            throw new NotImplementedException("Servicebus binding not implemented yet");
        } else {
            throw new RuntimeException("Unknown resource type");
        }
    }

    private Deployment getDeploymentCompositeForResource(BasicResourceNode resourceNode, ResourceCreationPlan resourceCreationPlan) {
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

    private FunctionApp getOrCreateFunctionAppFromList(CodegenResourceNode codegenResourceNode,
                                                       List<FunctionApp> functionApps) {
        String functionAppName = codegenResourceNode.getName();
        Optional<FunctionApp> first = functionApps.stream().filter(f -> f.getFunctionAppName().equals(functionAppName))
                .findFirst();

        if (first.isPresent()) {
            return first.get();
        } else {
            FunctionApp functionApp = new FunctionApp(functionAppName);
            functionApp.setLocalPortNumber(nextPortNumber);
            functionApps.add(functionApp);
            nextPortNumber++;

            return functionApp;
        }
    }
}
