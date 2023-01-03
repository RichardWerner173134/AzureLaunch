package com.werner.bl.codegeneration.model;

import com.werner.bl.codegeneration.model.enums.FunctionAppClientType;
import com.werner.bl.codegeneration.model.enums.FunctionAppTriggerType;
import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.EdgeType;
import org.springframework.stereotype.Component;

@Component
public class EdgeTypeMapper {

    public FunctionAppTriggerType computeResultingTriggerType (ResourceEdge edge) {
        ResourceType resourceType1 = edge.getResource1().getResourceType();
        ResourceType resourceType2 = edge.getResource2().getResourceType();
        EdgeType edgeType = edge.getEdgeType();

        if(resourceType1 == ResourceType.FUNCTION_APP && resourceType2 == ResourceType.FUNCTION_APP) {
            switch(edgeType) {
                case HTTP_GET:
                    return FunctionAppTriggerType.HTTP_GET;
                case HTTP_POST:
                    return FunctionAppTriggerType.HTTP_POST;
            }
        } else if(resourceType2 == ResourceType.FUNCTION_APP) {
            switch(edgeType) {
                case SERVICE_BUS_PUB_SUB:
                    return FunctionAppTriggerType.SERVICE_BUS_PUB_SUB;
                case SERVICE_BUS_QUEUE:
                    return FunctionAppTriggerType.SERVICE_BUS_QUEUE;
            }
        }
        throw new RuntimeException("Triggertype not found");
    }

    public FunctionAppClientType computeResultingClientType(ResourceEdge edge) {
        ResourceType resourceType1 = edge.getResource1().getResourceType();
        ResourceType resourceType2 = edge.getResource2().getResourceType();
        EdgeType edgeType = edge.getEdgeType();

        if(resourceType1 == ResourceType.FUNCTION_APP && resourceType2 == ResourceType.FUNCTION_APP) {
            switch(edgeType) {
                case HTTP_GET:
                    return FunctionAppClientType.HTTP_GET;
                case HTTP_POST:
                    return FunctionAppClientType.HTTP_POST;
            }
            throw new RuntimeException("Unknown EdgeType: " + edgeType);
        }

        throw new RuntimeException("Edge between FUNCTIONAPP and FUNCTION_APP doesn´t use a client. Use Trigger instead");
    }
}
