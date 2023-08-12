package pl.agh.edu.json.data_loader;

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
import pl.agh.edu.json.data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.json.data.ClientNumberModificationRandomTemporaryEventData;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONEventDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.EVENT_CONFIG.get();

	public static List<ClientNumberModificationCyclicTemporaryEventData> clientNumberModificationCyclicTemporaryEventData;
	public static List<ClientNumberModificationRandomTemporaryEventData> clientNumberModificationRandomTemporaryEventData;

	private JSONEventDataLoader() {}

	static {
		load();
	}

	public static void load() {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern("--MM-dd")
				.parseDefaulting(ChronoField.YEAR, 0)
				.toFormatter();
		clientNumberModificationCyclicTemporaryEventData = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "cyclic_temporary_events", JSONArray.class),
				e -> {
					JSONObject JSONEvent = (JSONObject) e;
					JSONObject JSONModifiers = (JSONObject) JSONEvent.get("modifiers");
					return new ClientNumberModificationCyclicTemporaryEventData(
							(String) JSONEvent.get("name"),
							(String) JSONEvent.get("calendar_description"),
							LocalDate.parse((String) (JSONEvent.get("start_date")), formatter),
							LocalDate.parse((String) (JSONEvent.get("end_date")), formatter),
							Stream.of(HotelVisitPurpose.values())
									.collect(Collectors.toMap(
											f -> f,
											f -> (double) JSONModifiers.get(f.toString()),
											(a, b) -> b,
											() -> new EnumMap<>(HotelVisitPurpose.class))));
				});

		clientNumberModificationRandomTemporaryEventData = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "random_temporary_events", JSONArray.class),
				e -> {
					JSONObject JSONEvent = (JSONObject) e;
					JSONObject JSONModifiers = (JSONObject) JSONEvent.get("modifiers");
					return new ClientNumberModificationRandomTemporaryEventData(
							(String) JSONEvent.get("name"),
							(String) JSONEvent.get("calendar_description"),
							(String) JSONEvent.get("popup_description"),
							JSONValueUtil.getInt((Long) JSONEvent.get("min_duration_days")),
							JSONValueUtil.getInt((Long) JSONEvent.get("max_duration_days")),
							Stream.of(HotelVisitPurpose.values())
									.collect(Collectors.toMap(
											f -> f,
											f -> (double) JSONModifiers.get(f.toString()),
											(a, b) -> b,
											() -> new EnumMap<>(HotelVisitPurpose.class))),
							(double) JSONEvent.get("occurrence_probability"),
							(String) JSONEvent.get("image_path")

				);
				});

	}
}
