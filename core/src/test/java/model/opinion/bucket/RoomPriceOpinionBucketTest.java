package model.opinion.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.model.opinion.bucket.RoomPriceOpinionBucket;

public class RoomPriceOpinionBucketTest {
	private RoomPriceOpinionBucket opinionBucket;

	@BeforeEach
	public void setUp() {
		opinionBucket = new RoomPriceOpinionBucket(3, new BigDecimal("1000"));
	}

	@ParameterizedTest
	@MethodSource("getValueTestArgs")
	public void getValueTest(BigDecimal offeredPrice, double expectedValue) {
		// Given
		opinionBucket.setPrices(offeredPrice);

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	private static Stream<Arguments> getValueTestArgs() {
		return Stream.of(
				Arguments.of(new BigDecimal("901"), 0.),
				Arguments.of(new BigDecimal("900"), 0.0),
				Arguments.of(new BigDecimal("890"), 0.1),
				Arguments.of(new BigDecimal("850"), 0.5),
				Arguments.of(new BigDecimal("800"), 1.),
				Arguments.of(new BigDecimal("799"), 1.)

		);
	}
}
