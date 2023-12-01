package pl.agh.edu.data.loader;

import static pl.agh.edu.data.extractor.JSONFilePath.HOTEL_CONFIG;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;

public class JSONHotelDataLoader {

	private static final String JSON_FILE_PATH = HOTEL_CONFIG.get();

	public static Map<String, Integer> initialData;
	public static Map<String, LocalTime> checkInAndOutTime;
	public static List<Room> initialRooms;

	static {
		load();
	}

	private JSONHotelDataLoader() {}

	public static void load() {
		initialData = JSONValueUtil.getMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "initial_data", JSONObject.class),
				entry -> (String) entry.getKey(),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()));
		checkInAndOutTime = JSONValueUtil.getMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "check_in_out_times", JSONObject.class),
				entry -> (String) entry.getKey(),
				entry -> JSONValueUtil.getLocalTime((String) entry.getValue()));
		initialRooms = JSONValueUtil.getListOfLists(
				JSONDataExtractor.extract(JSON_FILE_PATH, "initial_rooms", JSONArray.class),
				e -> {
					JSONObject roomJson = (JSONObject) e;
					RoomRank rank = RoomRank.valueOf((String) roomJson.get("rank"));
					RoomSize size = RoomSize.valueOf((String) roomJson.get("size"));
					int quantity = Integer.parseInt(roomJson.get("quantity").toString());

					return IntStream.range(0, quantity)
							.mapToObj(i -> new Room(rank, size))
							.collect(Collectors.toList());
				});
	}
}
