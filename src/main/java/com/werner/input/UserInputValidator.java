package com.werner.input;

import generated.internal.v1_0_0.model.AzCodegenRequest;
import org.springframework.stereotype.Component;

@Component
public class UserInputValidator {

	public void validateUserInput(AzCodegenRequest request) {
		validateObject(request);
		validateObject(request.getAppConfig());
		validateObject(request.getAzAccount());
		validateObject(request.getAzAccount().getServicePrincipal());
		validateObject(request.getAzAccount().getServicePrincipal().getServicePrincipalName());
		validateObject(request.getAzAccount().getServicePrincipal().getServicePrincipalAppId());
		validateObject(request.getAzAccount().getServicePrincipal().getServicePrincipalTenant());
		validateObject(request.getGraph());
		validateObject(request.getGraph().getEdges());
		validateObject(request.getGraph().getNodes());
	}

	private void validateObject(Object object) {
		if(object == null) {
			throw new IllegalStateException("Object is null: " + object.toString());
		}
	}
}
