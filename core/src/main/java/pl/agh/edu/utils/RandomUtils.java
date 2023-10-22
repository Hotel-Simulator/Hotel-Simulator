package pl.agh.edu.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import pl.agh.edu.model.time.Time;

public class RandomUtils {
	private static final int MAX_PROBABILITY = 100;

	private RandomUtils() {}

	public static int randomInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	public static int randomInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}

	public static int randomInt(int origin, int bound) {
		return ThreadLocalRandom.current().nextInt(origin, bound);
	}

	public static double randomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	public static double randomDouble(double bound) {
		return ThreadLocalRandom.current().nextDouble(bound);
	}

	public static double randomDouble(double origin, double bound) {
		return ThreadLocalRandom.current().nextDouble(origin, bound);
	}

	public static double randomGaussian(double mean, double standardDeviation) {
		return ThreadLocalRandom.current().nextGaussian() * standardDeviation + mean;
	}

	public static boolean randomBooleanWithProbability(double probability) {
		if (probability < 0 || probability > 1) {
			throw new IllegalArgumentException("Probability must be between 0 and 1.");
		}
		return ThreadLocalRandom.current().nextDouble() < probability;
	}

	public static <T> T randomArrayElement(T[] array) {
		return array[ThreadLocalRandom.current().nextInt(array.length)];
	}

	public static <T extends Enum<T>> T randomEnumElement(Class<T> enumClass) {
		return randomArrayElement(enumClass.getEnumConstants());
	}

	public static <T> T randomListElement(List<T> list) {
		return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
	}

	public static LocalDate randomDate(LocalDate origin, LocalDate bound) {
		if (!origin.isBefore(bound)) {
			throw new IllegalArgumentException("origin must be before bound.");
		}
		long daysBetween = ChronoUnit.DAYS.between(origin, bound);
		long randomDays = ThreadLocalRandom.current().nextLong(daysBetween);
		return origin.plusDays(randomDays);
	}

	public static LocalTime randomLocalTime(LocalTime origin, LocalTime bound) {
		if (!origin.isBefore(bound)) {
			throw new IllegalArgumentException("origin must be before bound.");
		}
		long minutesBetween = ChronoUnit.MINUTES.between(origin, bound);
		long randomMinutes = ThreadLocalRandom.current().nextLong(minutesBetween) / Time.timeUnitInMinutes * Time.timeUnitInMinutes;
		return origin.plusMinutes(randomMinutes);
	}

	public static LocalDateTime randomDateTime(LocalDateTime origin, long range, TemporalUnit unit) {
		if (range <= 0) {
			throw new IllegalArgumentException("range must be positive.");
		}
		long max = unit.getDuration().toSeconds() * range;
		long randomOffset = (ThreadLocalRandom.current().nextLong(max) / (Time.timeUnitInMinutes * 60) * (Time.timeUnitInMinutes * 60));
		return origin.plusSeconds(randomOffset);
	}

	public static LocalDateTime randomDateTime(LocalDateTime origin, LocalDateTime bound) {
		if (!origin.isBefore(bound)) {
			throw new IllegalArgumentException("origin must be before bound.");
		}
		return randomDateTime(origin, ChronoUnit.SECONDS.between(origin, bound), ChronoUnit.SECONDS);
	}

	public static <T> T randomKeyWithProbabilities(Map<T, Integer> constantsProbabilities) {
		if (constantsProbabilities.values().stream().mapToInt(i -> i).sum() != MAX_PROBABILITY) {
			throw new IllegalArgumentException("Probabilities must sum to 100.");
		}
		int a = 0;
		int randInt = ThreadLocalRandom.current().nextInt(MAX_PROBABILITY);
		for (Map.Entry<T, Integer> entry : constantsProbabilities.entrySet()) {
			a += entry.getValue();
			if (a > randInt) {
				return entry.getKey();
			}
		}
		return constantsProbabilities.keySet().stream().toList().get(0);
	}

}
