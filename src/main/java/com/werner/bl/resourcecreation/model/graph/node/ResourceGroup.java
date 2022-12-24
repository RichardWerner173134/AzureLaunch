package com.werner.bl.resourcecreation.model.graph.node;

import com.werner.bl.resourcecreation.model.ResourceType;
import lombok.Getter;


@Getter
public class ResourceGroup extends AbstractResourceNode {

    private String resourceGroupLocation;

    public ResourceGroup(String name, ResourceType resourceType, String resourceGroupLocation) {
        super(name, resourceType);

        this.resourceGroupLocation = resourceGroupLocation;
    }




}
