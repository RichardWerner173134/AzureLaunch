package com.werner.bl.resourcecreation.model.graph.edge;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.EdgeType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceEdge {

	private AbstractResourceNode resource1;

	private AbstractResourceNode resource2;

	private EdgeType edgeType;
}
