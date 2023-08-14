package pl.agh.edu.time_command;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class TimeCommand implements Comparable<TimeCommand> {
	private static final AtomicLong creationVersion = new AtomicLong(1L);
	protected final Runnable toExecute;
	protected LocalDateTime dueDateTime;
	private final Long version;

	public TimeCommand(Runnable toExecute, LocalDateTime dueDateTime) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
		this.version = creationVersion.getAndIncrement();
	}

	public void execute() {
		if (toExecute != null) {
			toExecute.run();
		}
	}

	public LocalDateTime getDueDateTime() {
		return dueDateTime;
	}

	@Override
	public int compareTo(TimeCommand other) {
		int dueDateTimeComparison = dueDateTime.compareTo(other.dueDateTime);
		if (dueDateTimeComparison != 0) {
			return dueDateTimeComparison;
		}

		return version.compareTo(other.version);
	}
}
