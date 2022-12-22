package com.werner.bl.resourcecreation.model;

import com.werner.bl.resourcecreation.model.graph.IDeployableResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResourceGroup implements IDeployableResource {
    private String resourceGroupName;

    private String resourceGroupLocation;
}
