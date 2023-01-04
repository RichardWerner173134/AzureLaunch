package com.werner.bl.codegeneration.model.enums;

import lombok.Getter;

@Getter
public enum ClientParam {
	AS_TARGET_URL("TargetUrl"),

	P_JSON_BODY("JsonBody")
	;

	private String value;

	ClientParam(String value) {
		this.value = value;
	}

}
