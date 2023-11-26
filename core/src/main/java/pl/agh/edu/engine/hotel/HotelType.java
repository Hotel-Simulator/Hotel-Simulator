package pl.agh.edu.engine.hotel;

import pl.agh.edu.serialization.KryoConfig;

public enum HotelType {
	CITY,
	RESORT,
	SANATORIUM;

	public static void kryoRegister() {
		KryoConfig.kryo.register(HotelType.class);
	}
}
