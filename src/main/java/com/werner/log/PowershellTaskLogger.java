package com.werner.log;

import com.werner.helper.FileUtil;
import com.werner.powershell.AbstractPowershellCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PowershellTaskLogger {

	private List<PowershellResponse> executedTasks;

	private final FileUtil fileUtil;

	private static Logger LOGGER = LoggerFactory
			.getLogger(PowershellTaskLogger.class);

	public PowershellTaskLogger(FileUtil fileUtil) {
		this.executedTasks = new ArrayList<>();
		this.fileUtil = fileUtil;
	}

	public void addLogItem(PowershellResponse powershellResponse, String taskName) {
		powershellResponse.setTaskName(taskName);
		executedTasks.add(powershellResponse);

		LOGGER.info("Taskname: " + powershellResponse.getTaskName() + " - ExitCode: " + powershellResponse.getExitCode());
	}

	public void saveLogsToFile() {
		String fileContent = buildFileContent();
		AbstractPowershellCaller abstractPowershellCaller = new AbstractPowershellCaller(this){};
		String tempDir = abstractPowershellCaller.getTempDir();
		fileUtil.writeContentToFile(tempDir + "\\logOutput.txt", fileContent);
	}

	private String buildFileContent() {
		return executedTasks.stream() //
				.map(task -> {
					String taskPart = "Taskname: " + task.getTaskName();
					String exitCode = "ExitCode: " + task.getExitCode();
					String logPart = "Log:\n" + task.getLog();
					String errorLogPart = "Errors:\n" + task.getErrorLog();
					return  taskPart + "\n\n" + exitCode + "\n\n" + logPart + "\n\n" + errorLogPart;
				}) //
				.collect(Collectors.joining("\n----------------------------------------------------------------------------\n"));
	}
}
