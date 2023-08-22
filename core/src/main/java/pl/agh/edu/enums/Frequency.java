package pl.agh.edu.enums;

import java.time.LocalDateTime;

import pl.agh.edu.model.time.Time;

public enum Frequency {
	EVERY_SHIFT,
	EVERY_TIME_TICK,
	EVERY_DAY,
	EVERY_MONTH,
	EVERY_YEAR;

	public LocalDateTime add(LocalDateTime localDateTime) {
		return switch (this) {
		case EVERY_SHIFT -> localDateTime.plusHours(8);
		case EVERY_TIME_TICK -> localDateTime.plusMinutes(Time.timeUnitInMinutes);
		case EVERY_DAY -> localDateTime.plusDays(1);
		case EVERY_MONTH -> localDateTime.plusMonths(1);
		case EVERY_YEAR -> localDateTime.plusYears(1);
		};
	}
}
