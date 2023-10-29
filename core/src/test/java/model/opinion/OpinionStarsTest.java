package model.opinion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.opinion.OpinionStars.FIVE;
import static pl.agh.edu.engine.opinion.OpinionStars.FOUR;
import static pl.agh.edu.engine.opinion.OpinionStars.FOUR_AND_HALF;
import static pl.agh.edu.engine.opinion.OpinionStars.HALF;
import static pl.agh.edu.engine.opinion.OpinionStars.ONE;
import static pl.agh.edu.engine.opinion.OpinionStars.ONE_AND_HALF;
import static pl.agh.edu.engine.opinion.OpinionStars.THREE;
import static pl.agh.edu.engine.opinion.OpinionStars.THREE_AND_HALF;
import static pl.agh.edu.engine.opinion.OpinionStars.TWO;
import static pl.agh.edu.engine.opinion.OpinionStars.TWO_AND_HALF;
import static pl.agh.edu.engine.opinion.OpinionStars.ZERO;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.engine.opinion.OpinionStars;

public class OpinionStarsTest {

	@ParameterizedTest
	@MethodSource("testGetWithValidValuesArgs")
	public void testGetWithValidValues(double input, OpinionStars expected) {
		OpinionStars result = OpinionStars.get(input);
		assertEquals(expected, result);
	}

	private static Stream<Arguments> testGetWithValidValuesArgs() {
		return Stream.of(
				Arguments.of(0.0, ZERO),
				Arguments.of(0.04, ZERO),
				Arguments.of(0.05, HALF),
				Arguments.of(0.1, HALF),
				Arguments.of(0.14, HALF),
				Arguments.of(0.15, ONE),
				Arguments.of(0.2, ONE),
				Arguments.of(0.24, ONE),
				Arguments.of(0.25, ONE_AND_HALF),
				Arguments.of(0.30, ONE_AND_HALF),
				Arguments.of(0.34, ONE_AND_HALF),
				Arguments.of(0.35, TWO),
				Arguments.of(0.40, TWO),
				Arguments.of(0.44, TWO),
				Arguments.of(0.45, TWO_AND_HALF),
				Arguments.of(0.50, TWO_AND_HALF),
				Arguments.of(0.54, TWO_AND_HALF),
				Arguments.of(0.55, THREE),
				Arguments.of(0.60, THREE),
				Arguments.of(0.64, THREE),
				Arguments.of(0.65, THREE_AND_HALF),
				Arguments.of(0.70, THREE_AND_HALF),
				Arguments.of(0.74, THREE_AND_HALF),
				Arguments.of(0.75, FOUR),
				Arguments.of(0.80, FOUR),
				Arguments.of(0.84, FOUR),
				Arguments.of(0.85, FOUR_AND_HALF),
				Arguments.of(0.90, FOUR_AND_HALF),
				Arguments.of(0.94, FOUR_AND_HALF),
				Arguments.of(0.95, FIVE),
				Arguments.of(1., FIVE));
	}

	@ParameterizedTest
	@MethodSource("testGetWithInvalidValuesArgs")
	public void testGetWithInvalidValues(double input, OpinionStars expected) {
		OpinionStars result = OpinionStars.get(input);
		assertEquals(expected, result);
	}

	private static Stream<Arguments> testGetWithInvalidValuesArgs() {
		return Stream.of(
				Arguments.of(-1.0, ZERO),
				Arguments.of(2.1, ZERO),
				Arguments.of(10.0, ZERO));
	}
}
