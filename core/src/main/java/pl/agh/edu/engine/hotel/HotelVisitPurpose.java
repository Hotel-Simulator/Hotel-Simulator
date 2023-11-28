package pl.agh.edu.engine.hotel;

import pl.agh.edu.serialization.KryoConfig;

public enum HotelVisitPurpose {
	VACATION,
	BUSINESS_TRIP,
	REHABILITATION;

	public static void kryoRegister() {
		KryoConfig.kryo.register(HotelVisitPurpose.class);
	}
}
