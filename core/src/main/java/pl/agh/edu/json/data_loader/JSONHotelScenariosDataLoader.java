package pl.agh.edu.json.data_loader;

import java.util.EnumMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONHotelScenariosDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.HOTEL_SCENARIOS_CONFIG.get();

	public static EnumMap<HotelVisitPurpose, Double> vacationVisitsMode;
	public static EnumMap<HotelVisitPurpose, Double> rehabilitationVisitsMode;
	public static EnumMap<HotelVisitPurpose, Double> businessVisitsMode;
	public static EnumMap<DifficultyLevel, Double> difficultyMultiplier;
	public static Map<HotelType, Map<Integer, Double>> vacationPopularity;

	private JSONHotelScenariosDataLoader() {}

	static {
		load();
	}

	public static void load() {

		vacationVisitsMode = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "seaside_resort", JSONObject.class),
				entry -> (Double) entry.getValue(),
				HotelVisitPurpose.class);

		rehabilitationVisitsMode = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "sanatorium_ciechocinek", JSONObject.class),
				entry -> (Double) entry.getValue(),
				HotelVisitPurpose.class);

		businessVisitsMode = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "city_center_hotel", JSONObject.class),
				entry -> (Double) entry.getValue(),
				HotelVisitPurpose.class);

		difficultyMultiplier = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "difficulty_multiplier", JSONObject.class),
				entry -> (Double) entry.getValue(),
				DifficultyLevel.class);

		vacationPopularity = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "clients_per_month_multiplier", JSONObject.class),
				entry -> JSONValueUtil.getMap(
						(JSONObject) entry.getValue(),
						entry2 -> Integer.parseInt((String) entry2.getKey()),
						entry2 -> JSONValueUtil.getDouble((Double) entry2.getValue())),
				HotelType.class);

	}

}
