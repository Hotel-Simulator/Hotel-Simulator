package pl.agh.edu.time_command;

import java.time.LocalDateTime;

public class TimeCommand implements Comparable<TimeCommand>{
    protected final Runnable toExecute;
    protected LocalDateTime dueDateTime;

    public TimeCommand(Runnable toExecute, LocalDateTime dueDateTime) {
        this.toExecute = toExecute;
        this.dueDateTime = dueDateTime;
    }
    public void execute() {
        toExecute.run();
    }
    @Override
    public int compareTo(TimeCommand other) {
        return dueDateTime.compareTo(other.dueDateTime);
    }
}
