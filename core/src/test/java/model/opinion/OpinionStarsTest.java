package model.opinion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.opinion.OpinionStars.FIVE;
import static pl.agh.edu.engine.opinion.OpinionStars.FOUR;
import static pl.agh.edu.engine.opinion.OpinionStars.ONE;
import static pl.agh.edu.engine.opinion.OpinionStars.THREE;
import static pl.agh.edu.engine.opinion.OpinionStars.TWO;

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
				Arguments.of(0., ONE),
				Arguments.of(0.20, ONE),
				Arguments.of(0.21, TWO),
				Arguments.of(0.40, TWO),
				Arguments.of(0.41, THREE),
				Arguments.of(0.60, THREE),
				Arguments.of(0.61, FOUR),
				Arguments.of(0.80, FOUR),
				Arguments.of(0.81, FIVE),
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
				Arguments.of(-1.0, ONE),
				Arguments.of(2.1, ONE),
				Arguments.of(10.0, ONE));
	}
}
