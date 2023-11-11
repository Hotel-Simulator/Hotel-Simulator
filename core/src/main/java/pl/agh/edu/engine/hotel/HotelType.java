package pl.agh.edu.engine.hotel;

import pl.agh.edu.serialization.KryoConfig;

public enum HotelType {
	HOTEL,
	RESORT,
	SANATORIUM;

	static {
		KryoConfig.kryo.register(HotelType.class);
	}
}
