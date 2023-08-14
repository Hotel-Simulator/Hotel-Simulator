package pl.agh.edu.model.console.commands;

import pl.agh.edu.model.console.Command;
import pl.agh.edu.model.console.LogHistory;
import pl.agh.edu.model.console.LogLevel;
import pl.agh.edu.model.time.Time;

public class StartTimeCommand implements Command {
	@Override
	public void execute() {
		Time time = Time.getInstance();
		LogHistory logHistory = LogHistory.getInstance();
		if (time.isRunning()) {
			logHistory.addEntry("Time is already running", LogLevel.ERROR);
		}
		time.start();
		logHistory.addEntry("Time started", LogLevel.SUCCESS);
	}
}
