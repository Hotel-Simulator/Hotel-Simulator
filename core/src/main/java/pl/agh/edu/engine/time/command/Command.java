package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Command implements Comparable<Command> {
	private static final AtomicLong creationVersion = new AtomicLong(1L);
	protected final SerializableRunnable toExecute;
	protected final Long version;
	private LocalDateTime dueDateTime;
	private Boolean toStop = false;

	public Command(SerializableRunnable toExecute, LocalDateTime dueDateTime) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
		this.version = creationVersion.getAndIncrement();
	}

	protected Command(SerializableRunnable toExecute, LocalDateTime dueDateTime, Long version) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
		this.version = version;
	}

	public abstract void execute();

	public abstract boolean isRequeueNeeded();

	public LocalDateTime getDueDateTime() {
		return dueDateTime;
	}

	protected void setDueDateTime(LocalDateTime dueDateTime) {
		this.dueDateTime = dueDateTime;
	}

	@Override
	public int compareTo(Command other) {
		int dueDateTimeComparison = getDueDateTime().compareTo(other.getDueDateTime());
		if (dueDateTimeComparison != 0) {
			return dueDateTimeComparison;
		}
		return version.compareTo(other.version);
	}

	public boolean isStoped() {
		return toStop;
	}

	public void stop() {
		toStop = true;
	}
}
