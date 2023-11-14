package pl.agh.edu.engine.employee;

import java.time.Duration;
import java.time.LocalTime;

import pl.agh.edu.serialization.KryoConfig;

public enum Shift {
	MORNING(LocalTime.of(8, 0)),
	EVENING(LocalTime.of(16, 0)),
	NIGHT(LocalTime.of(0, 0));

	private final LocalTime startTime;
	private final Duration duration = Duration.ofHours(8);

	static {
		KryoConfig.kryo.register(Shift.class);
	}

	Shift(LocalTime startTime) {
		this.startTime = startTime;
	}

	public boolean lasts(LocalTime time) {
		return switch (this) {
			case MORNING -> !time.isBefore(startTime) && time.isBefore(startTime.plus(duration));
			case EVENING -> !time.isBefore(startTime);
			case NIGHT -> time.isBefore(startTime.plus(duration));
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
