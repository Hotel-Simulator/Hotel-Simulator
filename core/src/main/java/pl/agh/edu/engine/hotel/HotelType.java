package pl.agh.edu.engine.hotel;

import pl.agh.edu.serialization.KryoConfig;

public enum HotelType {
	CITY,
	RESORT,
	SANATORIUM;

	static {
		KryoConfig.kryo.register(HotelType.class);
	}
}
