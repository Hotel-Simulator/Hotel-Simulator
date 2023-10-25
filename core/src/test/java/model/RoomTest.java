package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.agh.edu.engine.room.RoomRank.ECONOMIC;
import static pl.agh.edu.engine.room.RoomRank.STANDARD;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.room.RoomSize.FAMILY;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.room.Room;

public class RoomTest {

	private ClientGroup group;

	@BeforeEach
	void setUp() {
		group = mock(ClientGroup.class);
	}

	@Test
	public void changeRankTest_Success() {
		// Given
		Room room = new Room(ECONOMIC, DOUBLE);

		// When
		room.changeRank(STANDARD);

		// Then
		assertEquals(room.getRank(), STANDARD);
	}

	@Test
	public void changeRankTest_Failure() {
		// Given
		Room room = new Room(ECONOMIC, DOUBLE);

		// When

		// Then
		assertThrows(IllegalArgumentException.class, () -> room.changeRank(ECONOMIC));
	}

	@Test
	public void checkInResidentsTest_Success() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		when(group.getSize()).thenReturn(5);
		// When
		room.checkIn(group);

		// Then
		assertTrue(room.roomState.isOccupied());
	}

	@Test
	public void checkOutResidentsTest_Success() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
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
