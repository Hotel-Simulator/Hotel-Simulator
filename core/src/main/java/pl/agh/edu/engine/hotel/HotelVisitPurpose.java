package pl.agh.edu.engine.hotel;

import pl.agh.edu.serialization.KryoConfig;

public enum HotelVisitPurpose {
	VACATION,
	BUSINESS_TRIP,
	REHABILITATION;

	static {
		KryoConfig.kryo.register(HotelVisitPurpose.class);
	}
}
