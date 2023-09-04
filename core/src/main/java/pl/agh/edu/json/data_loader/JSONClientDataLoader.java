package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONClientDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.CLIENT_CONFIG.get();

	public static EnumMap<HotelVisitPurpose, Double> hotelVisitPurposeProbabilities;
	public static EnumMap<HotelVisitPurpose, EnumMap<RoomRank, Integer>> desiredRankProbabilities;
	public static EnumMap<HotelVisitPurpose, Map<Integer, Integer>> numberOfNightsProbabilities;
	public static EnumMap<HotelVisitPurpose, EnumMap<RoomCapacity, Integer>> roomCapacityProbabilities;
	public static EnumMap<RoomRank, EnumMap<RoomCapacity, BigDecimal>> averagePricesPerNight;
	public static Duration basicMaxWaitingTime;
	public static int waitingTimeVariation;

	private JSONClientDataLoader() {}

	static {
		load();
	}

	public static void load() {
		hotelVisitPurposeProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "hotel_visit_purpose_probabilities", JSONObject.class),
				entry -> (Double) entry.getValue(),
				HotelVisitPurpose.class);

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
		roomCapacityProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "room_capacity_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getEnumMap(
						(JSONObject) entry.getValue(),
						entry2 -> JSONValueUtil.getInt((Long) entry2.getValue()),
						RoomCapacity.class),
				HotelVisitPurpose.class);

		averagePricesPerNight = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "average_prices_per_nights", JSONObject.class),
				entry -> JSONValueUtil.getEnumMap(
						(JSONObject) entry.getValue(),
						entry2 -> JSONValueUtil.getBigDecimal((Long) entry2.getValue()),
						RoomCapacity.class),
				RoomRank.class);

		basicMaxWaitingTime = JSONValueUtil.getDuration(
				JSONDataExtractor.extract(JSON_FILE_PATH, "basic_max_waiting_time_in_minutes", Long.class));
		waitingTimeVariation = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "waiting_time_variation_in_minutes", Long.class));

	}

}
