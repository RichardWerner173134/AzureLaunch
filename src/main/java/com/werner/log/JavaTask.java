package com.werner.log;

import lombok.Getter;

@Getter
public class JavaTask extends AbstractTask {

	private String description;

	public JavaTask(String description) {
		this.description = description;
	}

	@Override
	protected String getShortLoggableDescription() {
		return "JTaskname: " + taskName;
	}

	@Override
	protected String getLongLoggableDescription() {
		return "JTaskname: " + taskName +  "\n\nDescription: " + description;
	}
}
