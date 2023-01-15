package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.resourcecreation.model.ResourceType;
import lombok.Getter;


@Getter
public class ResourceGroup extends AbstractResourceNode {

    private String resourceGroupLocation;

    public ResourceGroup(String name, String resourceGroupLocation) {
        super(name, ResourceType.RESOURCE_GROUP);

        this.resourceGroupLocation = resourceGroupLocation;
    }
}
