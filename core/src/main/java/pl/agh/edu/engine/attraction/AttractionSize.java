package pl.agh.edu.engine.attraction;

import pl.agh.edu.serialization.KryoConfig;

public enum AttractionSize {
	SMALL,
	MEDIUM,
	LARGE;

	static {
		KryoConfig.kryo.register(AttractionSize.class);
	}
}
