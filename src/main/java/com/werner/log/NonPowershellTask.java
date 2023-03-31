package com.werner.log;

import lombok.Getter;

@Getter
public class NonPowershellTask extends AbstractTask {

	private String description;

	public NonPowershellTask(String description) {
		this.description = description;
	}

	@Override
	protected String getShortLoggableDescription() {
		return "Taskname: " + taskName;
	}

	@Override
	protected String getLongLoggableDescription() {
		return "Taskname: " + taskName +  "\n\nDescription: " + description;
	}
}
