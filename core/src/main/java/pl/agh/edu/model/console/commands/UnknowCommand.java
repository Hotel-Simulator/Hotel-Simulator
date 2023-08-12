package pl.agh.edu.model.console.commands;

import pl.agh.edu.model.console.Command;
import pl.agh.edu.model.console.LogHistory;
import pl.agh.edu.model.console.LogLevel;

public class UnknowCommand implements Command {
	@Override
	public void execute() {
		LogHistory.getInstance().addEntry("Unknown command", LogLevel.ERROR);
	}
}
