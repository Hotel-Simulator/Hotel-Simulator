package pl.agh.edu.engine.advertisement;

import pl.agh.edu.serialization.KryoConfig;

public enum AdvertisementType {
	NEWSPAPER_ADVERTISEMENT,
	TV_ADVERTISEMENT,
	RADIO_ADVERTISEMENT,
	INTERNET_ADVERTISEMENT,
	FLYERS,
	WEB_PAGE,
	SOCIAL_MEDIA,
	BOOKING_PORTALS_PROFILE;

	public static void kryoRegister() {
		KryoConfig.kryo.register(AdvertisementType.class);
	}
}
