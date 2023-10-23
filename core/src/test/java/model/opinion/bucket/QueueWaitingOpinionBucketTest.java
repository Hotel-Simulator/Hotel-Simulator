package model.opinion.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.model.opinion.bucket.QueueWaitingOpinionBucket;

public class QueueWaitingOpinionBucketTest {
	private QueueWaitingOpinionBucket opinionBucket;

	@BeforeEach
	public void setUp() {
		Duration maxWaitingTime = Duration.ofMinutes(60);
		opinionBucket = new QueueWaitingOpinionBucket(3, maxWaitingTime);
	}

	@ParameterizedTest
	@MethodSource("testParameters")
	public void testGetValueWithParameters(LocalDateTime startDate, LocalDateTime endDate, double expectedValue) {
		// Given
		opinionBucket.setStartDate(startDate);
		if (endDate != null)
			opinionBucket.setEndDate(endDate);

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	private static Stream<Arguments> testParameters() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate1 = now.plusMinutes(15);

		LocalDateTime endDate2 = now.plusMinutes(29);
		LocalDateTime endDate3 = now.plusMinutes(30);
		LocalDateTime endDate4 = now.plusMinutes(40);

		LocalDateTime endDate5 = now.plusMinutes(45);

		return Stream.of(
				Arguments.of(now, endDate1, 1.0),
				Arguments.of(now, endDate2, 1.0),
				Arguments.of(now, endDate3, 1.0),
				Arguments.of(now, endDate4, 0.66),
				Arguments.of(now, endDate5, 0.5),
				Arguments.of(now, null, 0.)

		);
	}
}
