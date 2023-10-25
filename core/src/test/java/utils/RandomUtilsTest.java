package utils;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.utils.RandomUtils;

public class RandomUtilsTest {

	private enum TestEnum {
		VALUE1,
		VALUE2,
		VALUE3
	}

	@Test
	public void testRandomIntWithBound() {
		// Given
		int bound = 100;

		// When
		int randomInt = RandomUtils.randomInt(bound);

		// Then
		assertTrue(randomInt >= 0 && randomInt < bound);
	}

	@Test
	public void testRandomIntWithRange() {
		// Given
		int origin = 10;
		int bound = 20;

		// When
		int randomInt = RandomUtils.randomInt(origin, bound);

		// Then
		assertTrue(randomInt >= origin && randomInt < bound);
	}

	@ParameterizedTest()
	@MethodSource("provideRandomBooleanWithProbabilities")
	public void randomBooleanWithProbability(double probability, boolean expectedResult) {
		// When
		boolean actualResult = RandomUtils.randomBooleanWithProbability(probability);

		// Then
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testRandomDoubleWithBound() {
		// Given
		double bound = 100.0;

		// When
		double randomDouble = RandomUtils.randomDouble(bound);

		// Then
		assertTrue(Double.compare(randomDouble, 0.) > 0 && Double.compare(randomDouble, bound) < 0);
	}

	@Test
	public void testRandomDoubleWithRange() {
		// Given
		double origin = 10.0;
		double bound = 20.0;

		// When
		double randomDouble = RandomUtils.randomDouble(origin, bound);

		// Then
		assertTrue(Double.compare(randomDouble, 0.) > 0 && Double.compare(randomDouble, bound) < 0);
	}

	static Stream<Arguments> provideRandomBooleanWithProbabilities() {
		return Stream.of(
				Arguments.of(0, false),
				Arguments.of(1, true));
	}

	@ParameterizedTest
	@MethodSource("provideRandomBoolean_InvalidProbabilities")
	public void testRandomBoolean_InvalidProbability(double invalidProbability) {
		// Expect an IllegalArgumentException to be thrown
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomBooleanWithProbability(invalidProbability));
	}

	private static Stream<Arguments> provideRandomBoolean_InvalidProbabilities() {
		return Stream.of(
				Arguments.of(-0.1),
				Arguments.of(1.1));
	}

	@Test
	public void testRandomArrayElement() {
		// Given
		Integer[] array = {1, 2, 3, 4, 5};

		// When
		Integer randomElement = RandomUtils.randomArrayElement(array);

		// Then
		assertNotNull(randomElement);
		assertTrue(Arrays.asList(array).contains(randomElement));
	}

	@Test
	public void testRandomEnumElement() {
		// Given

		// When
		TestEnum randomEnum = RandomUtils.randomEnumElement(TestEnum.class);

		// Then
		assertNotNull(randomEnum);
		assertTrue(Arrays.asList(TestEnum.values()).contains(randomEnum));
	}

	@Test
	public void testRandomListElement() {
		// Given
		List<String> list = Arrays.asList("A", "B", "C");

		// When
		String randomElement = RandomUtils.randomListElement(list);

		// Then
		assertNotNull(randomElement);
		assertTrue(list.contains(randomElement));
	}

	@Test
	public void testRandomDate() {
		// Given
		LocalDate origin = LocalDate.of(2023, 1, 1);
		LocalDate bound = LocalDate.of(2023, 1, 2);

		// When
		LocalDate randomDate = RandomUtils.randomDate(origin, bound);

		// Then
		assertNotNull(randomDate);
		assertTrue(!randomDate.isBefore(origin) && randomDate.isBefore(bound));
	}

	@Test
	public void testRandomLocalTime() {
		// Given
		LocalTime origin = LocalTime.of(10, 0);
		LocalTime bound = LocalTime.of(10, 1);

		// When
		LocalTime randomTime = RandomUtils.randomLocalTime(origin, bound);

		// Then
		assertNotNull(randomTime);
		assertTrue(!randomTime.isBefore(origin) && randomTime.isBefore(bound));
	}

	@Test
	public void testRandomDate_InvalidParameters() {
		// Given
		LocalDate origin = LocalDate.of(2023, 1, 1);
		LocalDate bound = LocalDate.of(2023, 1, 1);

		// Then
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomDate(origin, bound));
	}

	@Test
	public void testRandomLocalTime_InvalidParameters() {
		// Given
		LocalTime origin = LocalTime.of(10, 0);
		LocalTime bound = LocalTime.of(10, 0);

		// Then
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomLocalTime(origin, bound));
	}

	@Test
	public void testRandomDateTimeWithRange() {
		// Given
		LocalDateTime origin = LocalDateTime.of(2023, 1, 1, 0, 0);
		long range = 60 * 100;

		// When
		LocalDateTime randomDateTime = RandomUtils.randomDateTime(origin, range, SECONDS);

		// Then
		assertNotNull(randomDateTime);
		assertTrue(!randomDateTime.isBefore(origin) && randomDateTime.isBefore(origin.plusMinutes(range + 1)));
	}

	@Test
	public void testRandomDateTimeWithRange_InvalidParameters() {
		// Given
		LocalDateTime origin = LocalDateTime.of(2023, 1, 1, 0, 0);
		long range = 0;

		// Then
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomDateTime(origin, range, HOURS));
	}

	@Test
	public void testRandomDateTimeWithBound() {
		// Given
		LocalDateTime origin = LocalDateTime.of(2023, 1, 1, 0, 0);
		LocalDateTime bound = LocalDateTime.of(2023, 1, 1, 0, 10);

		// When
		LocalDateTime randomDateTime = RandomUtils.randomDateTime(origin, bound);

		// Then
		assertNotNull(randomDateTime);
		assertTrue(!randomDateTime.isBefore(origin) && randomDateTime.isBefore(bound));
	}

	@Test
	public void testRandomDateTimeWithBound_InvalidParameters() {
		// Given
		LocalDateTime origin = LocalDateTime.of(2023, 1, 1, 0, 0);
		LocalDateTime bound = LocalDateTime.of(2022, 1, 1, 0, 0); // Origin after bound

		// Then
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomDateTime(origin, bound));
	}

	@Test
	public void testRandomKeyWithProbabilities() {
		// Given
		Map<String, Integer> probabilities = Map.of(
				"A", 50,
				"B", 30,
				"C", 20);

		// When
		String randomKey = RandomUtils.randomKeyWithProbabilities(probabilities);

		// Then
		assertNotNull(randomKey);
		assertTrue(probabilities.containsKey(randomKey));
	}

	@Test
	public void testRandomKeyWithInvalidProbabilities() {
		// Given
		Map<String, Integer> invalidProbabilities = Map.of(
				"A", 50);
		// Then
		assertThrows(IllegalArgumentException.class, () -> RandomUtils.randomKeyWithProbabilities(invalidProbabilities));
	}
}
