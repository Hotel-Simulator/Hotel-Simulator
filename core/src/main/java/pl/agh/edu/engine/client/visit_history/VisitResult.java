package pl.agh.edu.engine.client.visit_history;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public enum VisitResult {
	NO_ROOM_OF_WANTED_RANK("visit.result.noRoomOfWantedRank"),
	NO_ROOM_OF_WANTED_SIZE("visit.result.noRoomOfWantedSize"),
	NO_ROOM_OF_WANTED_RANK_AND_SIZE("visit.result.noRoomOfWantedRankAndSize"),
	PRICE_TO_HIGH("visit.result.priceToHigh"),
	GOT_ROOM("visit.result.gotRoom"),
	STEPPED_OUT_OF_QUEUE("visit.result.steppedOutOfQueue");

	public final LanguageString languageString;

	VisitResult(String string) {
		this.languageString = new LanguageString(string);
	}

	public static void kryoRegister() {
		KryoConfig.kryo.register(VisitResult.class);
	}
}
