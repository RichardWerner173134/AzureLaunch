package com.werner.input;

import com.werner.bl.exception.InvalidInputFileContentException;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import generated.internal.v1_0_0.model.GraphEdges;
import generated.internal.v1_0_0.model.GraphNodes;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInputValidator {

	public void validateUserInput(AzCodegenRequest request) throws InvalidInputFileContentException {
		validateObject(request, "root");
		validateObject(request.getAppConfig(), "root.appConfig");
		validateObject(request.getAzAccount(), "root.azAccount");
		validateObject(request.getAzAccount().getResourceGroupName(), "root.azAccount.resourceGroupName");
		validateObject(request.getAzAccount().getResourceGroupLocation(), "root.azAccount.resourceGroupLocation");
		validateObject(request.getAzAccount().getAppServicePlanName(), "root.azAccount.appServicePlanName");
		validateObject(request.getAzAccount().getServicePrincipalName(), "root.azAccount.servicePrincipalName");
		validateObject(request.getGraph(), "root.graph");
		validateObject(request.getGraph().getNodes(), "root.graph.nodes");
		validateObject(request.getGraph().getEdges(), "root.graph.edges");

		for (int i = 0; i < request.getGraph().getNodes().size(); i++) {
			GraphNodes node = request.getGraph().getNodes().get(i);
			validateObject(node.getName(), "root.graph.nodes[" + i + "].name");
			validateObject(node.getType(), "root.graph.nodes[" + i + "].type");
		}

		for (int i = 0; i < request.getGraph().getEdges().size(); i++) {
			GraphEdges edge = request.getGraph().getEdges().get(i);
			validateObject(edge.getType(), "root.graph.edges[" + i + "].type");
			validateObject(edge.getNode1(), "root.graph.edges[" + i + "].node1");
			validateObject(edge.getNode2(), "root.graph.edges[" + i + "].node2");
			validateEdgeNode(edge, request.getGraph().getNodes(), "root.graph.edges.[" + i + "]");
			// TODO refactor so that edge cannot contain the same nodeName twice
		}
	}

	private void validateObject(Object object, String objectName) throws InvalidInputFileContentException {
		if(object == null) {
			throw new InvalidInputFileContentException("Invalid Inputfile. Cannot find object: " + objectName);
		}
	}

	private void validateEdgeNode(GraphEdges edge, List<GraphNodes> nodes, String objectName) throws InvalidInputFileContentException {

		if(edge.getNode1().equals(edge.getNode2())) {
			throw new InvalidInputFileContentException("Invalid Inputfile. Nodes of an edge cant be equal: " + objectName);
		}

		boolean firstEdgeFound = false;
		boolean secondEdgeFound = false;

		for (GraphNodes node : nodes) {
			if(firstEdgeFound == false && node.getName().equals(edge.getNode1())) {
				firstEdgeFound = true;
			}

			if(secondEdgeFound == false && node.getName().equals(edge.getNode2())) {
				secondEdgeFound = true;
			}

			if(firstEdgeFound && secondEdgeFound) {
				return;
			}
		}

		String unknownNodeName;
		if(firstEdgeFound == false) {
			unknownNodeName = edge.getNode1();
		} else {
			unknownNodeName = edge.getNode2();
		}

		String exceptionMessage = "Invalid Inputfile. " + objectName + ".node" +
				(firstEdgeFound == false ? 1 : 2) + " is unknown: " + unknownNodeName;

		throw new InvalidInputFileContentException(exceptionMessage);
	}
}
