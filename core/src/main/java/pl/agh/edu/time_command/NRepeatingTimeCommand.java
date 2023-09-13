package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	public long getCounter() {
		return counter;
	}

	private long counter;

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, long N) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
	}

	@Override
	public void execute() {
		super.execute();
		if (--counter == 0) {
			stop();
		}
	}
}
