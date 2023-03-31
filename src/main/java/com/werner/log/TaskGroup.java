package com.werner.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskGroup extends AbstractTask {

	private List<AbstractTask> tasks;

	public TaskGroup(String taskName) {
		this.taskName = taskName;
		this.tasks = new ArrayList<>();
	}

	public void addTask(AbstractTask task) {
		tasks.add(task);
	}

	@Override
	protected String getShortLoggableDescription() {
		return "Start of Taskgroup: " + taskName;
	}

	@Override
	protected String getLongLoggableDescription() {
		return tasks.stream() //
				.map(task -> task.getLongLoggableDescription()) //
				.collect(Collectors.joining("\n----------------------------------------------------------------------------\n"));
	}
}
