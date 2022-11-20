package com.werner.bl.resourcecreation.model.graph.edge;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;

public class ResourceEdge {
	private AbstractResourceNode resource1;
	private AbstractResourceNode resource2;

	public ResourceEdge(AbstractResourceNode resource1, AbstractResourceNode resource2) {
		this.resource1 = resource1;
		this.resource2 = resource2;
	}

	public AbstractResourceNode getResource1() {
		return resource1;
	}

	public void setResource1(AbstractResourceNode resource1) {
		this.resource1 = resource1;
	}

	public AbstractResourceNode getResource2() {
		return resource2;
	}

	public void setResource2(AbstractResourceNode resource2) {
		this.resource2 = resource2;
	}
}
