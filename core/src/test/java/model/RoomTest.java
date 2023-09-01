package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class RoomTest {

	@Mock
	private ClientGroup group;

	@Test
	public void upgradeRankTest_Success() {
		// Given
		Room room = new Room(RoomRank.TWO, 5);

		// When
		room.upgradeRank();

		// Then
		assertEquals(room.getRank(), RoomRank.THREE);
	}

	@Test
	public void upgradeRankManySuccessTest_Success() {
		// Given
		Room room = new Room(RoomRank.ONE, 5);

		// When
		room.upgradeRankMany(3);

		// Then
		assertEquals(room.getRank(), RoomRank.FOUR);
		assertTrue(room.roomState.isBeingUpgraded());
	}

	@Test
	public void upgradeRankManyTest_Failure() {
		// Given
		Room room = new Room(RoomRank.ONE, 5);

		// When
		room.upgradeRankMany(6);

		// Then
		assertEquals(room.getRank(), RoomRank.ONE);
		assertFalse(room.roomState.isBeingUpgraded());
	}

	@Test
	public void checkInResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.THREE, 1);

		// When
		room.checkIn(group);

		// Then
		assertTrue(room.roomState.isOccupied());
	}

	@Test
	public void checkOutResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.THREE, 1);

		// When
		room.checkIn(group);
		room.checkOut();

		// Then
		assertFalse(room.roomState.isOccupied());
		assertTrue(room.roomState.isDirty());
	}
}
