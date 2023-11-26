package pl.agh.edu.engine.hotel.dificulty;

import pl.agh.edu.serialization.KryoConfig;

public enum DifficultyLevel {
	EASY,
	MEDIUM,
	HARD,
	BRUTAL;

	public static void kryoRegister() {
		KryoConfig.kryo.register(DifficultyLevel.class);
	}
}
