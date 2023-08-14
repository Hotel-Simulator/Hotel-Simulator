package pl.agh.edu.time_command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeCommandExecutor {
	private static TimeCommandExecutor instance;
	private final Map<LocalDateTime, List<TimeCommand>> commands;

	private TimeCommandExecutor() {
		this.commands = new HashMap<>();
	}

	public static TimeCommandExecutor getInstance() {
		if (instance == null)
			return new TimeCommandExecutor();
		return instance;
	}

	public void addCommand(LocalDateTime dateTime, TimeCommand timeCommand) {
		if (!commands.containsKey(dateTime)) {
			commands.put(dateTime, new ArrayList<>());
		}
		commands.get(dateTime).add(timeCommand);
	}

	public void executeCommands(LocalDateTime dateTime) {
		if (commands.containsKey(dateTime)) {
			commands.remove(dateTime).forEach(TimeCommand::execute);
		}
	}
}
