package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Map;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.utils.EnumPair;

public class JSONRoomDataLoader {

	private static final String JSON_FILE_PATH = JSONFilePath.ROOM_CONFIG.get();

	public static Duration roomRankChangeDuration;
	public static Duration roomBuildingDuration;
	public static Map<EnumPair<RoomRank, RoomSize>, BigDecimal> roomBuildingCosts;

	private JSONRoomDataLoader() {}

	static {
		load();
	}

	protected static void load() {
		roomRankChangeDuration = Duration.ofDays(JSONDataExtractor.extract(JSON_FILE_PATH, "room_rank_change_base_duration_in_days", Long.class));
		roomBuildingDuration = Duration.ofDays(JSONDataExtractor.extract(JSON_FILE_PATH, "room_building_base_duration_in_days", Long.class));
		var baseBuildCost = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "base_building_cost", Long.class));
		roomBuildingCosts = JSONValueUtil.convertMap(
				JSONValueUtil.getEnumMap(
						JSONDataExtractor.extract(JSON_FILE_PATH, "building_cost_multipliers", JSONObject.class),
						entry -> JSONValueUtil.getEnumMap(
								(JSONObject) entry.getValue(),
								entry2 -> baseBuildCost.multiply(
										JSONValueUtil.getBigDecimal(
												(Double) (entry2.getValue()))
												.setScale(2, RoundingMode.HALF_EVEN)).setScale(0, RoundingMode.HALF_EVEN),
								RoomSize.class),
						RoomRank.class));
	}

}
