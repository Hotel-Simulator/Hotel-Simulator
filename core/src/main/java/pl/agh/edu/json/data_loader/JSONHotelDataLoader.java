package pl.agh.edu.json.data_loader;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.Room;

public class JSONHotelDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.HOTEL_CONFIG.get();

	public static Map<String, Integer> initialData;
	public static Map<String, Long> attractivenessConstants;
	public static Map<String, LocalTime> checkInAndOutTime;

	public static List<Room> rooms;

	private JSONHotelDataLoader() {}

	static {
		load();
	}

	public static void load() {
		initialData = JSONValueUtil.getMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "initial_data", JSONObject.class),
				entry -> (String) entry.getKey(),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()));
		attractivenessConstants = JSONValueUtil.getMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "attractiveness_constants", JSONObject.class),
				entry -> (String) entry.getKey(),
				entry -> (Long) entry.getValue());
		checkInAndOutTime = JSONValueUtil.getMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "check_in_out_times", JSONObject.class),
				entry -> (String) entry.getKey(),
				entry -> JSONValueUtil.getLocalTime((String) entry.getValue()));
		rooms = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "rooms", JSONArray.class),
				e -> new Room(RoomRank.valueOf((String) ((JSONObject) e).get("rank")), JSONValueUtil.getInt((long) ((JSONObject) e).get("capacity"))));

	}
}
