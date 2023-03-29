package com.werner.log;

import lombok.Getter;

@Getter
public class PowershellResponse {
	private String taskName;

	private String log;

	private String errorLog;

	private int exitCode;

	public PowershellResponse(String log, String errorLog, int exitCode) {
		this.log = log;
		this.errorLog = errorLog;
		this.exitCode = exitCode;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
