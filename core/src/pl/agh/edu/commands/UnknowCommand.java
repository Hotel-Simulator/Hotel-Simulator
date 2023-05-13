package pl.agh.edu.commands;

import pl.agh.edu.command.Command;
import pl.agh.edu.console.LogHistory;
import pl.agh.edu.console.LogLevel;

public class UnknowCommand implements Command {
    @Override
    public void execute() {
        LogHistory.getInstance().addEntry("Unknown command", LogLevel.ERROR);
    }
}
