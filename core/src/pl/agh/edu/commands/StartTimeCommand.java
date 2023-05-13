package pl.agh.edu.commands;

import pl.agh.edu.command.Command;
import pl.agh.edu.console.LogHistory;
import pl.agh.edu.console.LogLevel;
import pl.agh.edu.time.Time;

public class StartTimeCommand implements Command {
    @Override
    public void execute() {
        Time time = Time.getInstance();
        LogHistory logHistory = LogHistory.getInstance();
        if(time.isRunning()){
            logHistory.addEntry("Time is already running", LogLevel.ERROR);
        }
        time.start();
        logHistory.addEntry("Time started", LogLevel.SUCCESS);
    }
}
