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

	public static Map<HotelType, Map<Integer, Double>> vacationPopularity;
	public static EnumMap<HotelType, EnumMap<HotelVisitPurpose, Double>> hotelTypeVisitProbabilities;

	private JSONHotelScenariosDataLoader() {}

	static {
		load();
	}

	public static void load() {

		hotelTypeVisitProbabilities = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "visit_probabilities", JSONObject.class),
				entry -> JSONValueUtil.getEnumMap(
						(JSONObject) entry.getValue(),
						entry2 -> (Double) entry2.getValue(),
						HotelVisitPurpose.class), HotelType.class);

		vacationPopularity = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "clients_per_month_multiplier", JSONObject.class),
				entry -> JSONValueUtil.getMap(
						(JSONObject) entry.getValue(),
						entry2 -> Integer.parseInt((String) entry2.getKey()),
						entry2 -> (Double) entry2.getValue()),
				HotelType.class);

	}

}
