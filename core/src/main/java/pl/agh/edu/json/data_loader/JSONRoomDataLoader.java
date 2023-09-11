package pl.agh.edu.json.data_loader;

import java.time.Duration;

import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;

public class JSONRoomDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.ROOM_CONFIG.get();

	public static Duration roomRankChangeDuration;
	public static Duration roomBuildingDuration;

	private JSONRoomDataLoader() {}

	static {
		load();
	}

	protected static void load() {
		roomRankChangeDuration = Duration.ofDays(JSONDataExtractor.extract(JSON_FILE_PATH, "room_rank_change_base_duration_in_days", Long.class));
		roomBuildingDuration = Duration.ofDays(JSONDataExtractor.extract(JSON_FILE_PATH, "room_building_base_duration_in_days", Long.class));
	}

}
