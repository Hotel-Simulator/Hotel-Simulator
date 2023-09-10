package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
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
		Room room = new Room(RoomRank.ECONOMIC, RoomSize.DOUBLE);

		// When
		room.changeRank(RoomRank.STANDARD);

		// Then
		assertEquals(room.getRank(), RoomRank.STANDARD);
	}

	@Test
	public void changeRankTest_Failure() {
		// Given
		Room room = new Room(RoomRank.ECONOMIC, RoomSize.DOUBLE);

		// When

		// Then
		assertThrows(IllegalArgumentException.class, () -> room.changeRank(RoomRank.ECONOMIC));
	}

	@Test
	public void checkInResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		when(group.getSize()).thenReturn(5);
		// When
		room.checkIn(group);

		// Then
		assertTrue(room.roomState.isOccupied());
	}

	@Test
	public void checkOutResidentsTest_Success() {
		// Given
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
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
