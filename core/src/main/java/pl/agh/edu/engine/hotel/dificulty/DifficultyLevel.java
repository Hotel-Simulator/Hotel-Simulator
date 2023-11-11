package pl.agh.edu.engine.hotel.dificulty;

import pl.agh.edu.serialization.KryoConfig;

public enum DifficultyLevel {
	EASY,
	MEDIUM,
	HARD,
	BRUTAL;

	static {
		KryoConfig.kryo.register(DifficultyLevel.class);
	}
}
