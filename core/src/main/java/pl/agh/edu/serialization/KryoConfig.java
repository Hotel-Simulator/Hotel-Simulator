package pl.agh.edu.serialization;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.engine.client.ClientGroup;

public class KryoConfig {

	public static final Kryo kryo = new Kryo();
	static {
		kryo.setReferences(true);

		kryo.register(BigDecimal.class);
		kryo.register(LocalDate.class);
		kryo.register(LocalDateTime.class);
		kryo.register(YearMonth.class);
		kryo.register(MonthDay.class);
		kryo.register(Year.class);
		kryo.register(Duration.class);
		kryo.register(LocalTime.class);
		kryo.register(ArrayList.class);
		kryo.register(LinkedList.class);
		kryo.register(PriorityQueue.class);
		kryo.register(EnumMap.class);
		kryo.register(Comparator.class);

		kryo.register(Object[].class);
		kryo.register(Class.class);
		kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());

		kryo.register(ClientGroup.class);

	}

	public static <T> Serializer<List<T>> listSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, List<T> object) {
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public List<T> read(Kryo kryo, Input input, Class<? extends List<T>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).toList();
			}
		};
	}

	public static <T> Serializer<PriorityQueue<T>> priorityQueueSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, PriorityQueue<T> object) {
				kryo.writeObject(output, object.comparator());
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public PriorityQueue<T> read(Kryo kryo, Input input, Class<? extends PriorityQueue<T>> type) {
				Comparator<T> comparator = kryo.readObject(input, Comparator.class);
				int size = kryo.readObject(input, Integer.class);
				PriorityQueue<T> priorityQueue = new PriorityQueue<>(comparator);
				IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).forEach(priorityQueue::add);
				return priorityQueue;
			}
		};
	}

	public static <T> Serializer<Set<T>> setSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, Set<T> object) {
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public Set<T> read(Kryo kryo, Input input, Class<? extends Set<T>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).collect(Collectors.toSet());
			}
		};
	}

	public static <T, K> Serializer<Map<T, K>> mapSerializer(Class<T> clazzT, Class<K> clazzK) {
		return new Serializer<>() {
			@Override
			public void write(Kryo kryo, Output output, Map<T, K> object) {
				kryo.writeObject(output, object.size());
				object.forEach((key, value) -> {
					kryo.writeObject(output, key);
					kryo.writeObjectOrNull(output, value, clazzK);
				});
			}

			@Override
			public Map<T, K> read(Kryo kryo, Input input, Class<? extends Map<T, K>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size)
						.mapToObj(i -> new AbstractMap.SimpleEntry<>(
								kryo.readObject(input, clazzT),
								kryo.readObjectOrNull(input, clazzK)))
						.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
			}
		};
	}

	public static <T, K> Serializer<Map<T, List<K>>> mapOfListSerializer(Class<T> clazzT, Class<K> clazzK) {
		return new Serializer<>() {
			@Override
			public void write(Kryo kryo, Output output, Map<T, List<K>> object) {
				kryo.writeObject(output, object.size());
				object.forEach((key, value) -> {
					kryo.writeObject(output, key);
					kryo.writeObjectOrNull(output, value, listSerializer(clazzK));
				});
			}

			@Override
			public Map<T, List<K>> read(Kryo kryo, Input input, Class<? extends Map<T, List<K>>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size)
						.mapToObj(i -> new AbstractMap.SimpleEntry<>(
								kryo.readObject(input, clazzT),
								kryo.readObjectOrNull(input, List.class, listSerializer(clazzK))))
						.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
			}
		};
	}

	public static <T> T getPrivateFieldValue(Object object, String fieldName, Class<T> clazz) {
		Field field;
		field = getFieldRecursively(object.getClass(), fieldName);

		assert field != null;
		field.setAccessible(true);
		try {
			return clazz.cast(field.get(object));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return getFieldRecursively(clazz.getSuperclass(), fieldName);
			}
		}
		return null;
	}
}
