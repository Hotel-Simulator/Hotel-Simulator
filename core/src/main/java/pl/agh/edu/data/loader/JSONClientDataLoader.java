package pl.agh.edu.data.loader;

import static pl.agh.edu.data.extractor.JSONFilePath.CLIENT_CONFIG;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.utils.Pair;

public class JSONClientDataLoader {
	private static final String JSON_FILE_PATH = CLIENT_CONFIG.get();

	public static EnumMap<HotelVisitPurpose, EnumMap<RoomRank, Integer>> desiredRankProbabilities;
	public static EnumMap<HotelVisitPurpose, Map<Integer, Integer>> numberOfNightsProbabilities;
	public static EnumMap<HotelVisitPurpose, Map<Integer, Integer>> clientGroupSizeProbabilities;
	public static Map<Pair<RoomRank, RoomSize>, BigDecimal> averagePricesPerNight;
	public static Duration basicMaxWaitingTime;
	public static int waitingTimeVariation;

	static {
		load();
	}

	private JSONClientDataLoader() {}

	public static void load() {

		desiredRankProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "desired_rank_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getEnumMap(
						(JSONObject) entry.getValue(),
						entry2 -> JSONValueUtil.getInt((Long) entry2.getValue()),
						RoomRank.class),
				HotelVisitPurpose.class);

		numberOfNightsProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "number_of_nights_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getMap(
						(JSONObject) entry.getValue(),
						entry2 -> Integer.parseInt((String) entry2.getKey()),
						entry2 -> JSONValueUtil.getInt((Long) entry2.getValue())),
				HotelVisitPurpose.class);
		clientGroupSizeProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "client_group_size_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getMap(
						(JSONObject) entry.getValue(),
						entry2 -> Integer.parseInt((String) entry2.getKey()),
						entry2 -> JSONValueUtil.getInt((Long) entry2.getValue())),
				HotelVisitPurpose.class);

		averagePricesPerNight = JSONValueUtil.convertMap(
				JSONValueUtil.getEnumMap(
						JSONDataExtractor.extract(JSON_FILE_PATH, "average_prices_per_nights", JSONObject.class),
						entry -> JSONValueUtil.getEnumMap(
								(JSONObject) entry.getValue(),
								entry2 -> JSONValueUtil.getBigDecimal((Long) entry2.getValue()),
								RoomSize.class),
						RoomRank.class));

		basicMaxWaitingTime = JSONValueUtil.getDuration(
				JSONDataExtractor.extract(JSON_FILE_PATH, "basic_max_waiting_time_in_minutes", Long.class));
		waitingTimeVariation = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "waiting_time_variation_in_minutes", Long.class));

	}

}
