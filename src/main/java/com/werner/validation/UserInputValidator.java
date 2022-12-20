package com.werner.validation;

import generated.internal.v1_0_0.model.AzCodegenRequest;
import org.springframework.stereotype.Component;

@Component
public class UserInputValidator {

	public void validateUserInput(AzCodegenRequest request) throws Exception {
		if(request == null) {
			throw new Exception("Request parsing failed");
		}
	}
}
