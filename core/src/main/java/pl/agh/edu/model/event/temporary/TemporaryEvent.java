package pl.agh.edu.model.event.temporary;

import java.time.LocalDate;

public abstract class TemporaryEvent {
	protected final LocalDate startDate;
	protected final LocalDate endDate;

	protected TemporaryEvent(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;

	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	@Override
	public String toString() {
		return "TemporaryEvent{" +
				"startDate=" + startDate +
				", endDate=" + endDate + '\'' +
				'}';
	}
}
