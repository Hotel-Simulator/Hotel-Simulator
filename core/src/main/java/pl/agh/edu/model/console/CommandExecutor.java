package pl.agh.edu.model.console;

import com.badlogic.gdx.utils.Queue;

import pl.agh.edu.model.console.commands.StartTimeCommand;
import pl.agh.edu.model.console.commands.UnknownCommand;

public class CommandExecutor {
	private static CommandExecutor instance = null;
	private final Queue<Command> commands = new Queue<>();

	private final LogHistory logHistory = LogHistory.getInstance();
	private final CommandHistory commandHistory = CommandHistory.getInstance();

	private CommandExecutor() {}

	public static CommandExecutor getInstance() {
		if (instance == null) {
			instance = new CommandExecutor();
		}
		return instance;
	}

	public void addCommand(String command) {
		logHistory.addEntry(command, LogLevel.DEFAULT);
		commandHistory.store(command);
		commands.addLast(parseCommand(command));
	}

	public void executeCommands() {
		while (commands.notEmpty()) {
			Command command = commands.removeFirst();
			command.execute();
		}
	}

	private Command parseCommand(String commandString) {
		if (commandString.equals("/time start")) {
			return new StartTimeCommand();
		} else {
			return new UnknownCommand();
		}
	}
}
