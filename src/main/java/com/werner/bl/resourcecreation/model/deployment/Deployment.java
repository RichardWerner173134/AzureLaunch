package com.werner.bl.resourcecreation.model.deployment;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Deployment {

	private List<AbstractResourceNode> deploymentComposite = new ArrayList<>();

}
