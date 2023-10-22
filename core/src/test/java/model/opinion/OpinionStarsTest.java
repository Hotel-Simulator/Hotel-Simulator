package model.opinion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.model.opinion.OpinionStars;

public class OpinionStarsTest {

	@ParameterizedTest
	@MethodSource("testGetWithValidValuesArgs")
	public void testGetWithValidValues(double input, OpinionStars expected) {
		OpinionStars result = OpinionStars.get(input);
		assertEquals(expected, result);
	}

	private static Stream<Arguments> testGetWithValidValuesArgs() {
		return Stream.of(
				Arguments.of(0.0, OpinionStars.ZERO),
				Arguments.of(0.04, OpinionStars.ZERO),
				Arguments.of(0.05, OpinionStars.HALF),
				Arguments.of(0.1, OpinionStars.HALF),
				Arguments.of(0.14, OpinionStars.HALF),
				Arguments.of(0.15, OpinionStars.ONE),
				Arguments.of(0.2, OpinionStars.ONE),
				Arguments.of(0.24, OpinionStars.ONE),
				Arguments.of(0.25, OpinionStars.ONE_AND_HALF),
				Arguments.of(0.30, OpinionStars.ONE_AND_HALF),
				Arguments.of(0.34, OpinionStars.ONE_AND_HALF),
				Arguments.of(0.35, OpinionStars.TWO),
				Arguments.of(0.40, OpinionStars.TWO),
				Arguments.of(0.44, OpinionStars.TWO),
				Arguments.of(0.45, OpinionStars.TWO_AND_HALF),
				Arguments.of(0.50, OpinionStars.TWO_AND_HALF),
				Arguments.of(0.54, OpinionStars.TWO_AND_HALF),
				Arguments.of(0.55, OpinionStars.THREE),
				Arguments.of(0.60, OpinionStars.THREE),
				Arguments.of(0.64, OpinionStars.THREE),
				Arguments.of(0.65, OpinionStars.THREE_AND_HALF),
				Arguments.of(0.70, OpinionStars.THREE_AND_HALF),
				Arguments.of(0.74, OpinionStars.THREE_AND_HALF),
				Arguments.of(0.75, OpinionStars.FOUR),
				Arguments.of(0.80, OpinionStars.FOUR),
				Arguments.of(0.84, OpinionStars.FOUR),
				Arguments.of(0.85, OpinionStars.FOUR_AND_HALF),
				Arguments.of(0.90, OpinionStars.FOUR_AND_HALF),
				Arguments.of(0.94, OpinionStars.FOUR_AND_HALF),
				Arguments.of(0.95, OpinionStars.FIVE),
				Arguments.of(1., OpinionStars.FIVE));
	}

	@ParameterizedTest
	@MethodSource("testGetWithInvalidValuesArgs")
	public void testGetWithInvalidValues(double input, OpinionStars expected) {
		OpinionStars result = OpinionStars.get(input);
		assertEquals(expected, result);
	}

	private static Stream<Arguments> testGetWithInvalidValuesArgs() {
		return Stream.of(
				Arguments.of(-1.0, OpinionStars.ZERO),
				Arguments.of(2.1, OpinionStars.ZERO),
				Arguments.of(10.0, OpinionStars.ZERO));
	}
}
