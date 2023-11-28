package pl.agh.edu.engine.client.visit_history;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public enum VisitResult {
	HOTEL_DOES_NOT_OFFER_ANY_ROOMS(new LanguageString("clientGroupVisitResultHistory.visitResult.hotelDoesNotOfferAnyRooms")),
	HOTEL_DOES_NOT_OFFER_ROOMS_OF_WANTED_SIZE_AND_TYPE(new LanguageString("clientGroupVisitResultHistory.visitResult.hotelDoesNotOfferRoomsOfWantedSizeAndType")),
	ALL_ROOMS_OF_WANTED_SIZE_AND_TYPE_CURRENTLY_OCCUPIED(new LanguageString("clientGroupVisitResultHistory.visitResult.allRoomsOfWantedSizeAndTypeCurrentlyOccupied")),
	PRICE_TO_HIGH(new LanguageString("clientGroupVisitResultHistory.visitResult.priceToHigh")),
	GOT_ROOM(new LanguageString("clientGroupVisitResultHistory.visitResult.gotRoom")),
	STEPPED_OUT_OF_QUEUE(new LanguageString("clientGroupVisitResultHistory.visitResult.steppedOutOfQueue"));

	public final LanguageString languageString;

	VisitResult(LanguageString languageString) {
		this.languageString = languageString;
	}

	public static void kryoRegister() {
		KryoConfig.kryo.register(VisitResult.class);
	}
}
