package pl.agh.edu.data.loader;

import static pl.agh.edu.data.extractor.JSONFilePath.HOTEL_SCENARIOS_CONFIG;

import java.util.EnumMap;
import java.util.Map;

import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public class JSONHotelScenariosDataLoader {
	private static final String JSON_FILE_PATH = HOTEL_SCENARIOS_CONFIG.get();

	public static Map<HotelType, Map<Integer, Double>> vacationPopularity;
	public static EnumMap<HotelType, EnumMap<HotelVisitPurpose, Double>> hotelTypeVisitProbabilities;
	public static EnumMap<HotelType, AttractivenessConstantsData> attractivenessConstants;

	static {
		load();
	}

	private JSONHotelScenariosDataLoader() {}

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

		attractivenessConstants = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "attractiveness_constants", JSONObject.class),
				entry -> {
					JSONObject value = (JSONObject) entry.getValue();
					return new AttractivenessConstantsData(
							(Long) value.get("local_market"),
							(Long) value.get("local_attractions"));
				},
				HotelType.class);

	}

}
