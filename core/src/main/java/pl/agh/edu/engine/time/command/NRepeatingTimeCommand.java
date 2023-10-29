package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import pl.agh.edu.engine.time.Frequency;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	private final Runnable toExecuteAfterLastRepetition;
	private long counter;

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N, Runnable toExecuteAfterLastRepetition) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N) {
		this(frequency, toExecute, dueTime, N, () -> {});
	}

	public long getCounter() {
		return counter;
	}

	@Override
	public void execute() {
		super.execute();
		if (--counter == 0 && !toStop) {
			stop();
			toExecuteAfterLastRepetition.run();
		}
	}
}
