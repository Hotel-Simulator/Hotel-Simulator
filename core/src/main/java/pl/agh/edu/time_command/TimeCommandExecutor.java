package pl.agh.edu.time_command;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class TimeCommandExecutor {
	private static final TimeCommandExecutor instance = new TimeCommandExecutor();
	private final PriorityQueue<TimeCommand> commands;

	private TimeCommandExecutor() {
		this.commands = new PriorityQueue<>();
	}

	public static TimeCommandExecutor getInstance() {
		return instance;
	}

	public void addCommand(TimeCommand timeCommand) {
		commands.add(timeCommand);
	}

	public void executeCommands(LocalDateTime dateTime) {
		while (!commands.isEmpty() && !commands.peek().dueDateTime.isAfter(dateTime)) {
			TimeCommand command = commands.poll();
			command.execute();
		}
	}
}
