package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	public long getCounter() {
		return counter;
	}

	private long counter;
	private final Runnable toExecuteAfterLast;

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N, Runnable toExecuteAfterLast) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
		this.toExecuteAfterLast = toExecuteAfterLast;
	}

	@Override
	public void execute() {
		super.execute();
		if (--counter == 0) {
			stop();
			toExecuteAfterLast.run();
		}
	}
}
