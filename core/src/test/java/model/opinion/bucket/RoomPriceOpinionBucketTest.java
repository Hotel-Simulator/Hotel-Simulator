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
		opinionBucket = new RoomPriceOpinionBucket(3);
	}

	@ParameterizedTest
	@MethodSource("getValueTestArgs")
	public void getValueTest(BigDecimal offeredPrice, BigDecimal maxPrice, double expectedValue) {
		// Given
		opinionBucket.setPrices(maxPrice, offeredPrice);

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	private static Stream<Arguments> getValueTestArgs() {
		return Stream.of(
				Arguments.of(new BigDecimal("901"), new BigDecimal("1000"), 0.),
				Arguments.of(new BigDecimal("900"), new BigDecimal("1000"), 0.0),
				Arguments.of(new BigDecimal("890"), new BigDecimal("1000"), 0.1),
				Arguments.of(new BigDecimal("850"), new BigDecimal("1000"), 0.5),
				Arguments.of(new BigDecimal("800"), new BigDecimal("1000"), 1.),
				Arguments.of(new BigDecimal("799"), new BigDecimal("1000"), 1.)

		);
	}
}
