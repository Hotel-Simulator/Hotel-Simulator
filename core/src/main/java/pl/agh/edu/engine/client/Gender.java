package pl.agh.edu.engine.client;

import pl.agh.edu.serialization.KryoConfig;

public enum Gender {
	MALE,
	FEMALE,
	OTHER;

	static {
		KryoConfig.kryo.register(Gender.class);
	}
}
