package pl.agh.edu.time_command;

import java.time.LocalDateTime;

import pl.agh.edu.model.time.Time;

public class RepeatingTimeCommand extends TimeCommand {

	private final Frequency frequency;
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private static final Time time = Time.getInstance();

	public RepeatingTimeCommand(Frequency frequency, Runnable toExecute) {
		super(toExecute);
		this.frequency = frequency;
	}

	@Override
	public final void execute() {
		toExecute.run();
		timeCommandExecutor.addCommand(frequency.add(time.getTime()), this);
	}

	public enum Frequency {
		EVERY_SHIFT, EVERY_DAY, EVERY_MONTH, EVERY_YEAR;

		LocalDateTime add(LocalDateTime localDateTime) {
			return switch (this) {
			case EVERY_SHIFT -> localDateTime.plusHours(8);
			case EVERY_DAY -> localDateTime.plusDays(1);
			case EVERY_MONTH -> localDateTime.plusMonths(1);
			case EVERY_YEAR -> localDateTime.plusYears(1);
			};
		}
	}

}
