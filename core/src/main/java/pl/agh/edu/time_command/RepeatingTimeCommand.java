package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;

public class RepeatingTimeCommand extends TimeCommand {
	private final Frequency frequency;
	private Boolean toStop = false;
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	public RepeatingTimeCommand(Frequency frequency, Runnable toExecute, LocalDateTime dueTime) {
		super(toExecute, dueTime);
		this.frequency = frequency;
	}

	@Override
	public void execute() {
		if (!toStop) {
			toExecute.run();
			repeat();
		}
	}

	private void repeat() {
		if (!toStop) {
			updateDueDateTime();
			timeCommandExecutor.addCommand(this);
		}
	}

	private void updateDueDateTime() {
		dueDateTime = frequency.add(dueDateTime);
	}

	public void stop() {
		toStop = true;
	}
}
