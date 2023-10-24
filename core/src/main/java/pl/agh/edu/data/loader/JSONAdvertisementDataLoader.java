package pl.agh.edu.data.loader;

import static java.math.RoundingMode.HALF_EVEN;
import static pl.agh.edu.data.extractor.JSONFilePath.ADVERTISEMENT_CONFIG;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.data.type.AdvertisementData;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public class JSONAdvertisementDataLoader {
	private static final String JSON_FILE_PATH = ADVERTISEMENT_CONFIG.get();

	public static BigDecimal multiplier;
	public static EnumMap<AdvertisementType, AdvertisementData> advertisementData;

	static {
		load();
	}

	private JSONAdvertisementDataLoader() {}

	public static void load() {
		multiplier = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "multiplier", Double.class)).setScale(4, HALF_EVEN);

		advertisementData = JSONValueUtil.getEnumMap(
				JSONDataExtractor.extract(JSON_FILE_PATH, "advertisements", JSONObject.class),
				entry -> {
					JSONObject data = (JSONObject) entry.getValue();
					JSONObject effectivenessData = (JSONObject) data.get("effectiveness");
					return new AdvertisementData(
							AdvertisementType.valueOf((String) entry.getKey()),
							JSONValueUtil.getBigDecimal((Long) data.get("cost_per_day")),
							Arrays.stream(HotelVisitPurpose.values()).collect(Collectors.toMap(
									h -> h,
									h -> multiplier.multiply(JSONValueUtil.getBigDecimal((Long) effectivenessData.get(h.toString()))).setScale(4, HALF_EVEN),
									(a, b) -> b,
									() -> new EnumMap<>(HotelVisitPurpose.class))));
				},
				AdvertisementType.class);
	}
}
