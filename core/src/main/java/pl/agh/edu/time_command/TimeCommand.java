package pl.agh.edu.time_command;

public class TimeCommand {
    protected final Runnable toExecute;

    public TimeCommand(Runnable toExecute) {
        this.toExecute = toExecute;
    }

    public void execute() {
        toExecute.run();
    }
}
