package pl.agh.edu.engine.attraction;

import pl.agh.edu.serialization.KryoConfig;

public enum AttractionState {
	ACTIVE,
	BEING_BUILD,
	CHANGING_SIZE,
	SHUTTING_DOWN,
	INACTIVE,
	OPENING;

	public static void kryoRegister() {
		KryoConfig.kryo.register(AttractionState.class);
	}
}
