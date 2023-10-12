package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data.ClientNumberModificationRandomEventData;
import pl.agh.edu.json.data.CyclicEventData;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.language.LanguageManager;
import pl.agh.edu.model.event.ClientNumberModifier;

public class JSONEventDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.EVENT_CONFIG.get();

	public static List<CyclicEventData> cyclicEventData;
	public static List<ClientNumberModificationRandomEventData> clientNumberModificationRandomEventData;

	private JSONEventDataLoader() {}

	static {
		load();
	}

	public static void load() {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern("--MM-dd")
				.parseDefaulting(ChronoField.YEAR, 0)
				.toFormatter();
		cyclicEventData = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "cyclic_temporary_events", JSONArray.class),
				e -> {
					JSONObject JSONEvent = (JSONObject) e;
					return new CyclicEventData(
							LanguageManager.get((String) JSONEvent.get("title")),
							LanguageManager.get((String) JSONEvent.get("event_appearance_popup_description")),
							LanguageManager.get((String) JSONEvent.get("event_start_popup_description")),
							LanguageManager.get((String) JSONEvent.get("calendar_description")),
							(String) JSONEvent.get("image_path"),
							LocalDate.parse((String) (JSONEvent.get("start_date")), formatter));
				});

		clientNumberModificationRandomEventData = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "random_temporary_events", JSONArray.class),
				e -> {
					JSONObject JSONEvent = (JSONObject) e;
					JSONObject JSONModifiers = (JSONObject) JSONEvent.get("modifier");
					return new ClientNumberModificationRandomEventData(
							LanguageManager.get((String) JSONEvent.get("title")),
							LanguageManager.get((String) JSONEvent.get("event_appearance_popup_description")),
							LanguageManager.get((String) JSONEvent.get("event_start_popup_description")),
							LanguageManager.get((String) JSONEvent.get("calendar_description")),
							JSONValueUtil.getInt((Long) JSONEvent.get("min_duration_days")),
							JSONValueUtil.getInt((Long) JSONEvent.get("max_duration_days")),
							new ClientNumberModifier(
									Stream.of(HotelVisitPurpose.values())
											.collect(Collectors.toMap(
													f -> f,
													f -> BigDecimal.valueOf((Double) JSONModifiers.get(f.toString()))
															.setScale(4, RoundingMode.HALF_EVEN)
															.stripTrailingZeros(),
													(a, b) -> b,
													() -> new EnumMap<>(HotelVisitPurpose.class)))),
							(double) JSONEvent.get("occurrence_probability"),
							(String) JSONEvent.get("image_path")

				);
				});
	}
}
