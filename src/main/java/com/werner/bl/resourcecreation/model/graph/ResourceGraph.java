package com.werner.bl.resourcecreation.model.graph;

import com.werner.bl.resourcecreation.model.graph.edge.ResourceEdge;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.bl.resourcecreation.model.graph.node.ServicePrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceGraph {

	private ResourceGroup resourceGroup;

	private String appServicePlanName;

	private ServicePrincipal servicePrincipal;

	private List<AbstractResourceNode> nodes = new ArrayList<>();

	private List<ResourceEdge> edges = new ArrayList<>();
}
