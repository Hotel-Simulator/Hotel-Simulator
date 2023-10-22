package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	public long getCounter() {
		return counter;
	}

	private long counter;
	private final Runnable toExecuteAfterLastRepetition;

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N, Runnable toExecuteAfterLastRepetition) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N) {
		this(frequency, toExecute, dueTime, N, () -> {});
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
