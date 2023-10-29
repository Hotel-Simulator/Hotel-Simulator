package model.opinion.bucket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;

public class RoomBreakingOpinionBucketTest {
	private RoomBreakingOpinionBucket opinionBucket;

	@BeforeEach
	public void setUp() {
		opinionBucket = new RoomBreakingOpinionBucket(3);
	}

	@Test
	public void testGetValueWhenRoomIsNotBroken() {
		// Given
		double expectedValue = 1.0;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	@Test
	public void testGetValueWhenRoomIsBroken() {
		// Given
		opinionBucket.roomBroke();
		double expectedValue = 0.0;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	@Test
	public void testGetValueWhenRoomIsBrokenAndRepaired() {
		// Given
		opinionBucket.roomBroke();
		opinionBucket.roomRepaired();
		double expectedValue = 0.5;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}
}
