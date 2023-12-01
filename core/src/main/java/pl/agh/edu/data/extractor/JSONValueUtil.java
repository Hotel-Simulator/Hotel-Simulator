package pl.agh.edu.data.extractor;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.utils.Pair;

public class JSONValueUtil {

	private JSONValueUtil() {}

	public static <R> List<R> getList(JSONArray jsonArray, Function<Object, R> mapper) {
		return Stream.of(jsonArray.toArray())
				.map(mapper)
				.collect(Collectors.toList());
	}

	public static <R> List<R> getListOfLists(JSONArray jsonArray, Function<Object, List<R>> mapper) {
		return Stream.of(jsonArray.toArray())
				.map(mapper)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	public static LocalDate getLocalDate(String stringDate) {
		return LocalDate.parse(stringDate, ISO_LOCAL_DATE);
	}

	public static LocalTime getLocalTime(String stringTime) {
		return LocalTime.parse(stringTime, ISO_LOCAL_TIME);
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

	public static <F, S, V> Map<Pair<F, S>, V> convertMap(Map<F, Map<S, V>> inputMap) {
		return inputMap.entrySet().stream()
				.flatMap(outerEntry -> outerEntry.getValue().entrySet().stream()
						.map(innerEntry -> new AbstractMap.SimpleEntry<>(Pair.of(outerEntry.getKey(), innerEntry.getKey()), innerEntry.getValue())))
				.collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
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
