package com.werner.log;

import lombok.Getter;

@Getter
public abstract class AbstractTask {
	protected String taskName;

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	protected abstract String getShortLoggableDescription();

	protected abstract String getLongLoggableDescription();
}
