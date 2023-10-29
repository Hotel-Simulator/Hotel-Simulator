package pl.agh.edu.data.loader;

import java.math.BigDecimal;
import java.time.Duration;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONFilePath;
import pl.agh.edu.data.extractor.JSONValueUtil;

public class JSONOpinionDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.OPINION_CONFIG.get();

	public static double opinionProbabilityForClientWhoGotRoom;
	public static double opinionProbabilityForClientWhoDidNotGetRoom;
	public static double opinionProbabilityForClientWhoSteppedOutOfQueue;
	public static Duration opinionHoldingDuration;
	public static BigDecimal opinionChangeMultiplier;
	public static double desiredPriceModifier;
	public static double maxWaitingTimeModifier;

	private JSONOpinionDataLoader() {}

	static {
		load();
	}

	public static void load() {

		opinionProbabilityForClientWhoGotRoom = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_got_room", Double.class);
		opinionProbabilityForClientWhoDidNotGetRoom = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_did_no_get_room", Double.class);
		opinionProbabilityForClientWhoSteppedOutOfQueue = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_stepped_out_of_queue", Double.class);
		opinionHoldingDuration = Duration.ofDays(JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_holding_duration_in_days", Long.class));
		opinionChangeMultiplier = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_change_multiplier", Double.class));
		desiredPriceModifier = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_desired_price_modifier", Double.class);
		maxWaitingTimeModifier = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_max_waiting_time_modifier", Double.class);
	}

}
