package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.TimeCommandExecutor;

public class RepeatingTimeCommand extends TimeCommand {
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	protected final Frequency frequency;
	protected Boolean toStop = false;

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

	protected void repeat() {
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
