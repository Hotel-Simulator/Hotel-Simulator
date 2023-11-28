package pl.agh.edu.engine.attraction;

import pl.agh.edu.serialization.KryoConfig;

public enum AttractionSize {
	SMALL,
	MEDIUM,
	LARGE;

	public static void kryoRegister() {
		KryoConfig.kryo.register(AttractionSize.class);
	}
}
