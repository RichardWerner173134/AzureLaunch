package com.werner.log;

import com.werner.helper.FileUtil;
import com.werner.powershell.AbstractPowershellCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskLogger {

	private List<AbstractTask> executedTasks;

	private final FileUtil fileUtil;

	private static Logger LOGGER = LoggerFactory.getLogger(TaskLogger.class);

	public TaskLogger(FileUtil fileUtil) {
		this.executedTasks = new ArrayList<>();
		this.fileUtil = fileUtil;
	}

	public void addLogItem(AbstractTask task, String taskName) {
		task.setTaskName(taskName);
		executedTasks.add(task);

		String shortLog = task.getShortLoggableDescription();

		LOGGER.info(shortLog);
	}

	public void saveLogsToFile() {
		String fileContent = buildFileContent();
		AbstractPowershellCaller abstractPowershellCaller = new AbstractPowershellCaller(this){};
		String tempDir = abstractPowershellCaller.getTempDir(); // TODO
		try {
			fileUtil.writeContentToFile(tempDir + "\\logOutput.txt", fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String buildFileContent() {
		return executedTasks.stream() //
				.map(AbstractTask::getLongLoggableDescription) //
				.collect(Collectors.joining("\n----------------------------------------------------------------------------\n"));
	}
}
