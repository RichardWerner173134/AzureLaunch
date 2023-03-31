package com.werner.log;

import lombok.Getter;

@Getter
public class PowershellTask extends AbstractTask {

	private String log;

	private String errorLog;

	private int exitCode;

	public PowershellTask(String log, String errorLog, int exitCode) {
		this.log = log;
		this.errorLog = errorLog;
		this.exitCode = exitCode;
	}

	@Override
	protected String getShortLoggableDescription() {
		return "PSTaskname: " + this.getTaskName() + " - ExitCode: " + this.getExitCode();
	}

	@Override
	protected String getLongLoggableDescription() {
		String renderedTaskName = "PSTaskname: " + taskName;
		String renderedExitCode = "ExitCode: " + exitCode;
		String renderedLogPart = "Log:\n" + log;
		String renderedErrorLogPart = "Errors:\n" + errorLog;
		return renderedTaskName + "\n\n" + renderedExitCode + "\n\n" + renderedLogPart + "\n\n" + renderedErrorLogPart;
	}
}
