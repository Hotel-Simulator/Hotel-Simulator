package pl.agh.edu.json.data_loader;

import java.time.Duration;
import java.util.EnumMap;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONRoomDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.ROOM_CONFIG.get();

	public static int maxCapacity;
	public static EnumMap<RoomRank, Duration> upgradeTimes;

	private JSONRoomDataLoader() {}

	static {
		load();
	}

	protected static void load() {
		maxCapacity = JSONValueUtil.getInt(JSONDataExtractor.extract(JSON_FILE_PATH, "max_capacity", Long.class));
		upgradeTimes = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "upgrade_times_in_hours", JSONObject.class),
				(entry -> Duration.ofHours((Long) entry.getValue())),
				RoomRank.class);

	}

}
