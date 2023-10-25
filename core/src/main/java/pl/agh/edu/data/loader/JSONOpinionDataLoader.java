package pl.agh.edu.json.data_loader;

import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;

public class JSONOpinionDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.OPINION_CONFIG.get();

	public static double opinionProbabilityForClientWhoGotRoom;
	public static double opinionProbabilityForClientWhoDidNotGetRoom;
	public static double opinionProbabilityForClientWhoSteppedOutOfQueue;

	private JSONOpinionDataLoader() {}

	static {
		load();
	}

	public static void load() {

		opinionProbabilityForClientWhoGotRoom = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_got_room", Double.class);
		opinionProbabilityForClientWhoDidNotGetRoom = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_did_no_get_room", Double.class);
		opinionProbabilityForClientWhoSteppedOutOfQueue = JSONDataExtractor.extract(JSON_FILE_PATH, "opinion_probability_for_clients_who_stepped_out_of_queue", Double.class);

	}

}
