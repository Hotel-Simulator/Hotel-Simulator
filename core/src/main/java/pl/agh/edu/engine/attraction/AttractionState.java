package pl.agh.edu.engine.attraction;

import pl.agh.edu.serialization.KryoConfig;

public enum AttractionState {
	ACTIVE,
	BEING_BUILD,
	CHANGING_SIZE,
	SHUTTING_DOWN,
	INACTIVE,
	OPENING;

	static {
		KryoConfig.kryo.register(AttractionState.class);
	}
}
