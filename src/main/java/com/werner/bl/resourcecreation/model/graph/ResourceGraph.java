package com.werner.bl.resourcecreation.model.graph;

import java.util.List;

public class ResourceGraph {
	private List<AbstractResourceNode> nodes;
	private List<ResourceEdge> edges;

	public ResourceGraph(List<AbstractResourceNode> nodes, List<ResourceEdge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}

	public List<AbstractResourceNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<AbstractResourceNode> nodes) {
		this.nodes = nodes;
	}

	public List<ResourceEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<ResourceEdge> edges) {
		this.edges = edges;
	}
}
