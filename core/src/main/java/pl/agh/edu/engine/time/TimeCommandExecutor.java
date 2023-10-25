package pl.agh.edu.engine.time;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import pl.agh.edu.engine.time.command.TimeCommand;

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
		while (!commands.isEmpty() && !commands.peek().getDueDateTime().isAfter(dateTime)) {
			TimeCommand command = commands.poll();
			command.execute();
		}
	}
}
