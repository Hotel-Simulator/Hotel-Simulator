package pl.agh.edu.model.employee;

import java.time.LocalTime;

public enum Shift {
	MORNING,
	EVENING,
	NIGHT;

	public LocalTime getStartTime() {
		return switch (this) {
		case MORNING -> LocalTime.of(8, 0);
		case EVENING -> LocalTime.of(16, 0);
		case NIGHT -> LocalTime.of(0, 0);
		};
	}

	public LocalTime getEndTime() {
		return switch (this) {
		case MORNING -> LocalTime.of(16, 0);
		case EVENING -> LocalTime.of(0, 0);
		case NIGHT -> LocalTime.of(8, 0);
		};
	}

	public Shift next() {
		return switch (this) {
		case MORNING -> EVENING;
		case EVENING -> NIGHT;
		case NIGHT -> MORNING;
		};
	}

}
