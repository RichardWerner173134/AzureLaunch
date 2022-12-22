package com.werner.bl.resourcecreation.model;

import com.werner.bl.resourcecreation.model.deployment.Deployment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceCreationPlan {

	private List<Deployment> deployments = new ArrayList<>();

}
