package pl.agh.edu.json.data_extractor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONValueUtil {

	private JSONValueUtil() {}

	public static <R> List<R> getList(JSONArray jsonArray, Function<Object, R> mapper) {
		return Stream.of(jsonArray.toArray())
				.map(mapper)
				.collect(Collectors.toList());
	}

	public static LocalDate getLocalDate(String stringDate) {
		return LocalDate.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public static LocalTime getLocalTime(String stringTime) {
		return LocalTime.parse(stringTime, DateTimeFormatter.ISO_LOCAL_TIME);
	}

	public static Duration getDuration(Long durationInMinutes) {
		return Duration.ofMinutes(durationInMinutes);
	}

	public static int getInt(Long value) {
		return value.intValue();
	}

	public static BigDecimal getBigDecimal(Long value) {
		return BigDecimal.valueOf(value);
	}

	public static BigDecimal getBigDecimal(Double value) {
		return BigDecimal.valueOf(value);
	}

	public static <K, V> Map<K, V> getMap(
			JSONObject jsonObject,
			Function<Map.Entry<?, ?>, K> keyMapper,
			Function<Map.Entry<?, ?>, V> valueMapper) {
		return Arrays.stream(jsonObject.entrySet().toArray())
				.map(o -> (Map.Entry<?, ?>) o)
				.collect(Collectors.toMap(
						keyMapper,
						valueMapper,
						(a, b) -> b));
	}

	public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(
			JSONObject jsonObject,
			Function<EnumMap.Entry<?, ?>, V> valueMapper,
			Class<K> enumClass) {
		return Arrays.stream(jsonObject.entrySet().toArray())
				.map(o -> (Map.Entry<?, ?>) o)
				.collect(Collectors.toMap(
						entry -> K.valueOf(enumClass, entry.getKey().toString()),
						valueMapper,
						(a, b) -> b,
						() -> new EnumMap<>(enumClass)));
	}

}
