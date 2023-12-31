package pl.agh.edu.engine.time;

import java.util.Arrays;

import pl.agh.edu.serialization.KryoConfig;

public enum PartOfDay {
	MORNING(6),
	DAY(10),
	EVENING(17),
	NIGHT(22);

	public final int startHour;

	public static void kryoRegister() {
		KryoConfig.kryo.register(PartOfDay.class);
	}

	PartOfDay(int startHour) {
		this.startHour = startHour;
	}

	public static PartOfDay parseHour(int ofHour) {
		return Arrays.stream(PartOfDay.values())
				.filter(partOfDay -> {
					if (partOfDay.startHour < (partOfDay.startHour + partOfDay.getDuration()) % 24) {
						return ofHour >= partOfDay.startHour && ofHour < (partOfDay.startHour + partOfDay.getDuration()) % 24;
					} else {
						return ofHour >= partOfDay.startHour || ofHour < (partOfDay.startHour + partOfDay.getDuration()) % 24;
					}
				})
				.findFirst()
				.orElseThrow();
	}

	public int getDuration() {
		PartOfDay[] values = PartOfDay.values();
		int nextStartHour = values[(this.ordinal() + 1) % values.length].startHour;
		return (nextStartHour - startHour + 24) % 24;
	}

	public int getEndHour() {
		return (this.startHour + this.getDuration()) % 24;
	}
}
