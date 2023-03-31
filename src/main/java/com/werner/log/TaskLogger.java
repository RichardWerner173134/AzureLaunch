package com.werner.log;

import com.werner.helper.FileUtil;
import com.werner.powershell.AbstractPowershellCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskLogger {

	private List<AbstractTask> executedTasks;

	private TaskGroup currentTaskGroup;

	private final FileUtil fileUtil;

	private static Logger LOGGER = LoggerFactory.getLogger(TaskLogger.class);

	public TaskLogger(FileUtil fileUtil) {
		this.executedTasks = new ArrayList<>();
		this.fileUtil = fileUtil;
	}

	public void addLogItem(AbstractTask task, String taskName) {
		task.setTaskName(taskName);
		String shortLog = task.getShortLoggableDescription();

		if(currentTaskGroup != null) {
			currentTaskGroup.addTask(task);
			shortLog = "\t└─" + shortLog;
		} else {
			executedTasks.add(task);
		}
		LOGGER.info(shortLog);
	}

	public void beginNewTaskGroup(String taskGroupDescription) {
		currentTaskGroup = new TaskGroup(taskGroupDescription);
		executedTasks.add(currentTaskGroup);
		LOGGER.info(currentTaskGroup.getShortLoggableDescription());
	}

	public void endTaskGroup() {
		currentTaskGroup = null;
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
				.map(new Function<AbstractTask, String>() {
					@Override
					public String apply(AbstractTask abstractTask) {
						String logContent = abstractTask.getLongLoggableDescription();
						if (abstractTask instanceof TaskGroup) {
							String renderedTaskGroupname = abstractTask.taskName + " ║\n";
							String line2 = "";
							for (int i = 0; i < renderedTaskGroupname.length() - 2; i++) {
								line2 += "═";
							}
							renderedTaskGroupname += line2 + "╝\n\n";
							logContent = renderedTaskGroupname + logContent;
						}
						return logContent;
					}
				}) //
				.collect(Collectors.joining("\n════════════════════════════════════════════════════════════════════════════\n"));
	}
}
