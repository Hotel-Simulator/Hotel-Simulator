package pl.agh.edu.engine.time;

import java.time.LocalDateTime;

import pl.agh.edu.serialization.KryoConfig;

public enum Frequency {
	EVERY_SHIFT,
	EVERY_TIME_TICK,
	EVERY_PART_OF_DAY,
	EVERY_DAY,
	EVERY_MONTH,
	EVERY_YEAR;

	static {
		KryoConfig.kryo.register(Frequency.class);
	}

	public LocalDateTime add(LocalDateTime localDateTime) {
		return switch (this) {
			case EVERY_SHIFT -> localDateTime.plusHours(8);
			case EVERY_TIME_TICK -> localDateTime.plusMinutes(Time.timeUnitInMinutes);
			case EVERY_DAY -> localDateTime.plusDays(1);
			case EVERY_MONTH -> localDateTime.plusMonths(1);
			case EVERY_YEAR -> localDateTime.plusYears(1);
			case EVERY_PART_OF_DAY ->
				localDateTime.plusHours((PartOfDay.parseHour(localDateTime.getHour()).getEndHour() - localDateTime.getHour() + 24) % 24);
		};
	}
}
