package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data.AdvertisementData;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.advertisement.AdvertisementType;

public class JSONAdvertisementDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.ADVERTISEMENT_CONFIG.get();

	public static BigDecimal multiplier;
	public static EnumMap<AdvertisementType, AdvertisementData> advertisementData;

	private JSONAdvertisementDataLoader() {}

	static {
		load();
	}

	public static void load() {
		multiplier = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "multiplier", Double.class)).setScale(4, RoundingMode.HALF_EVEN);

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
									h -> multiplier.multiply(JSONValueUtil.getBigDecimal((Long) effectivenessData.get(h.toString()))).setScale(4, RoundingMode.HALF_EVEN),
									(a, b) -> b,
									() -> new EnumMap<>(HotelVisitPurpose.class))));
				},
				AdvertisementType.class);
	}
}
