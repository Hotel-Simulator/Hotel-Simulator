package model.opinion.bucket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.model.opinion.bucket.RoomCleaningOpinionBucket;

public class RoomCleaningOpinionBucketTest {
	private RoomCleaningOpinionBucket opinionBucket;

	@BeforeEach
	public void setUp() {
		opinionBucket = new RoomCleaningOpinionBucket(3, 5);
	}

	@Test
	public void testGetValueWhenRoomIsNotCleaned() {
		// Given
		double expectedValue = 0.;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	@Test
	public void testGetValueWhenRoomIsCleaned() {
		// Given
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();

		double expectedValue = 1.;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}

	@Test
	public void testGetValueWithMixedRooms() {
		// Given
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();
		opinionBucket.setRoomCleaned();
		double expectedValue = 0.3;

		// When
		double actualValue = opinionBucket.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.01);
	}
}
