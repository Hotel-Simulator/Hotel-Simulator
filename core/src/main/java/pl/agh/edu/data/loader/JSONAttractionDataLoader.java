package pl.agh.edu.data.loader;

import static pl.agh.edu.data.extractor.JSONFilePath.ATTRACTION_CONFIG;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.utils.Pair;

public class JSONAttractionDataLoader {
	private static final String JSON_FILE_PATH = ATTRACTION_CONFIG.get();

	public static EnumMap<AttractionSize, Integer> dailyCapacity;
	public static EnumMap<AttractionSize, BigDecimal> dailyExpenses;
	public static BigDecimal incomePerClient;
	public static EnumMap<AttractionSize, BigDecimal> buildCost;
	public static EnumMap<AttractionSize, Duration> buildDuration;
	public static Map<Pair<AttractionType, HotelVisitPurpose>, Double> chancesOfVisit;
	public static Map<Pair<AttractionType, AttractionSize>, EnumMap<HotelVisitPurpose, BigDecimal>> modifier;
	static {
		load();
	}

	private JSONAttractionDataLoader() {}

	public static void load() {
		dailyCapacity = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "daily_capacity", JSONObject.class),
				entry -> JSONValueUtil.getInt((Long) entry.getValue()),
				AttractionSize.class);
		dailyExpenses = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "daily_capacity", JSONObject.class),
				entry -> JSONValueUtil.getBigDecimal((Long) entry.getValue()),
				AttractionSize.class);
		incomePerClient = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "income_per_client", Long.class));
		buildCost = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "build_cost", JSONObject.class),
				entry -> JSONValueUtil.getBigDecimal((Long) entry.getValue()),
				AttractionSize.class);
		buildDuration = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "build_duration_in_days", JSONObject.class),
				entry -> Duration.ofDays((Long) entry.getValue()),
				AttractionSize.class);
		chancesOfVisit = JSONValueUtil.convertMap(
				JSONValueUtil.getEnumMap(
						JSONDataExtractor.extract(JSON_FILE_PATH, "chances_of_visit", JSONObject.class),
						entry -> JSONValueUtil.getEnumMap(
								(JSONObject) entry.getValue(),
								entry2 -> (Double) (entry2.getValue()),
								HotelVisitPurpose.class),
						AttractionType.class));
		var modifierBaseValues = JSONValueUtil.convertMap(
				JSONValueUtil.getEnumMap(
						JSONDataExtractor.extract(JSON_FILE_PATH, "modifier_base_values", JSONObject.class),
						entry -> JSONValueUtil.getEnumMap(
								(JSONObject) entry.getValue(),
								entry2 -> JSONValueUtil.getBigDecimal((Double) (entry2.getValue())),
								HotelVisitPurpose.class),
						AttractionType.class));
		var modifierMultiplier = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "modifier_multiplier", JSONObject.class),
				entry -> JSONValueUtil.getBigDecimal((Double) entry.getValue()),
				AttractionSize.class);

		modifier = Arrays.stream(AttractionType.values())
				.flatMap(type -> Arrays.stream(AttractionSize.values()).map(size -> Pair.of(type, size)))
				.collect(Collectors.toMap(
						pair -> pair,
						pair -> Arrays.stream(HotelVisitPurpose.values()).collect(Collectors.toMap(
								hotelVisitPurpose -> hotelVisitPurpose,
								hotelVisitPurpose -> modifierBaseValues.get(Pair.of(pair.first(), hotelVisitPurpose))
										.multiply(modifierMultiplier.get(pair.second())),
								(a, b) -> b,
								() -> new EnumMap<>(HotelVisitPurpose.class)))));
	}
}
