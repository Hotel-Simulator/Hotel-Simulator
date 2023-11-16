package pl.agh.edu.engine.attraction;

import pl.agh.edu.serialization.KryoConfig;

public enum AttractionType {
	RESTAURANT,
	SWIMMING_POOL,
	SPA;

	static {
		KryoConfig.kryo.register(AttractionType.class);
	}
}
