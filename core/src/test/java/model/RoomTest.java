package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class RoomTest {

	private ClientGroup group;

	@BeforeEach
	void setUp() {
		group = mock(ClientGroup.class);
	}

	@Test
	public void changeRankTest_Success() {
		// Given
		Room room = new Room(RoomRank.TWO, RoomCapacity.FIVE);

		// When
		room.changeRank(RoomRank.THREE);

		// Then
		assertEquals(room.getRank(), RoomRank.THREE);
	}

	@Test
	public void changeRankTest_Failure() {
		// Given
		Room room = new Room(RoomRank.ONE, RoomCapacity.FIVE);

		// When

		// Then
		assertThrows(IllegalArgumentException.class, () -> room.changeRank(RoomRank.ONE));
	}

	@Test
	public void checkInResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.FIVE);
		when(group.getSize()).thenReturn(5);
		// When
		room.checkIn(group);

		// Then
		assertTrue(room.roomState.isOccupied());
	}

	@Test
	public void checkInResidentsTest_Failure() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.FIVE);
		when(group.getSize()).thenReturn(4);

		// When;

		// Then
		assertThrows(IllegalArgumentException.class, () -> room.checkIn(group));
	}

	@Test
	public void checkOutResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		when(group.getSize()).thenReturn(3);

		// When
		room.checkIn(group);
		room.checkOut();

		// Then
		assertFalse(room.roomState.isOccupied());
		assertTrue(room.roomState.isDirty());
		assertNull(room.getResidents());
	}
}
