package pl.agh.edu.serialization;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

public class KryoConfig {

	public static final Kryo kryo = new Kryo();
	static {
		kryo.setReferences(true);

		kryo.register(BigDecimal.class);
		kryo.register(LocalDate.class);
		kryo.register(LocalDateTime.class);
		kryo.register(YearMonth.class);
		kryo.register(Year.class);
		kryo.register(Duration.class);
		kryo.register(LocalTime.class);
		kryo.register(ArrayList.class);

		kryo.register(Object[].class);
		kryo.register(Class.class);
		kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());

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

	public static void setPrivateFieldValue(Object object, String fieldName, Object valueTobeSet) {
		Field field;
		field = getFieldRecursively(object.getClass(), fieldName);

		assert field != null;
		field.setAccessible(true);
		try {
			field.set(object, valueTobeSet);
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
