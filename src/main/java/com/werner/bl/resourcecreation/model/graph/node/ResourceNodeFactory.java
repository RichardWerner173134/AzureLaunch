package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.resourcecreation.model.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class ResourceNodeFactory {

	public AbstractResourceNode create(String name, ResourceType type){
		if(type == ResourceType.FUNCTION || type == ResourceType.FUNCTION_APP) {
			return new CodegenResourceNode(name, type);
		} else {
			return new BasicResourceNode(name, type);
		}
	}
}