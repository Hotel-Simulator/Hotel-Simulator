package pl.agh.edu.time_command;

import java.time.LocalDateTime;

public class TimeCommand implements Comparable<TimeCommand>{
    protected final Runnable toExecute;
    protected LocalDateTime dueDateTime;
    private final LocalDateTime createdDateTime = LocalDateTime.now();

    public TimeCommand(Runnable toExecute, LocalDateTime dueDateTime) {
        this.toExecute = toExecute;
        this.dueDateTime = dueDateTime;
    }
    public void execute() {
        toExecute.run();
    }
    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }
    @Override
    public int compareTo(TimeCommand other) {
        if(dueDateTime.equals(other.dueDateTime))
            return createdDateTime.compareTo(other.createdDateTime);
        return dueDateTime.compareTo(other.dueDateTime);
    }
}
