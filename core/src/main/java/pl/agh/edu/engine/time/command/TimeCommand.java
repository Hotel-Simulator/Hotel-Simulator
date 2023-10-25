package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class TimeCommand implements Comparable<TimeCommand> {
	private static final AtomicLong creationVersion = new AtomicLong(1L);
	protected final Runnable toExecute;
	private final Long version = creationVersion.getAndIncrement();
	protected LocalDateTime dueDateTime;

	public TimeCommand(Runnable toExecute, LocalDateTime dueDateTime) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
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
