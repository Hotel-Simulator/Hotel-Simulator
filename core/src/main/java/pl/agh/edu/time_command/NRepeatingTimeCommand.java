package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	public int getCounter() {
		return counter;
	}

	private int counter;

	public NRepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime, int N) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
	}

	@Override
	public void execute() {
		if (!toStop) {
			toExecute.run();
			repeat();
		}
		if (--counter == 0) {
			stop();
		}
	}
}
