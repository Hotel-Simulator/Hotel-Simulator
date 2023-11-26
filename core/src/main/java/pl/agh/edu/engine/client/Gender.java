package pl.agh.edu.engine.client;

import pl.agh.edu.serialization.KryoConfig;

public enum Gender {
	MALE,
	FEMALE,
	OTHER;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Gender.class);
	}
}
