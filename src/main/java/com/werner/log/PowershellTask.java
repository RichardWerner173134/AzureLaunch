package com.werner.log;

import lombok.Getter;

@Getter
public class PowershellTask extends AbstractTask {
	private String command;

	private String log;

	private String errorLog;

	private int exitCode;

	public PowershellTask(String log, String errorLog, int exitCode) {
		// TODO add command as parameter
		this.command = "asdasd";
		this.log = log;
		this.errorLog = errorLog;
		this.exitCode = exitCode;
	}

	@Override
	protected String getShortLoggableDescription() {
		return "Taskname: " + this.getTaskName() + " - ExitCode: " + this.getExitCode();
	}

	@Override
	protected String getLongLoggableDescription() {
		String renderedTaskName = "Taskname: " + taskName;
		String renderedExitCode = "ExitCode: " + exitCode;
		String renderedLogPart = "Log:\n" + log;
		String renderedErrorLogPart = "Errors:\n" + errorLog;
		return renderedTaskName + "\n\n" + renderedExitCode + "\n\n" + renderedLogPart + "\n\n" + renderedErrorLogPart;
	}
}
