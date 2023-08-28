package model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class RoomTest {

	@Mock
	private ClientGroup group;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void upgradeRankTest() {
		Room room = new Room(RoomRank.TWO, 5, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.upgradeRank();

		assertEquals(room.getRank(), RoomRank.THREE);
	}

	@Test
	public void upgradeRankManySuccessTest() {
		Room room = new Room(RoomRank.ONE, 5, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.upgradeRankMany(3);

		assertEquals(room.getRank(), RoomRank.FOUR);
		assertTrue(room.getRoomStates().isBeingUpgraded());
	}

	@Test
	public void upgradeRankManyFailTest() {
		Room room = new Room(RoomRank.ONE, 5, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.upgradeRankMany(6);

		assertEquals(room.getRank(), RoomRank.ONE);
		assertFalse(room.getRoomStates().isBeingUpgraded());
	}

	@Test
	public void checkInResidentsTest() {

		Room room = new Room(RoomRank.THREE, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

		room.checkIn(group);

		assertTrue(room.getRoomStates().isOccupied());
	}

	@Test
	public void checkOutResidentsTest() {

		Room room = new Room(RoomRank.THREE, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

		room.checkIn(group);
		room.checkOut();

		assertFalse(room.getRoomStates().isOccupied());
		assertTrue(room.getRoomStates().isDirty());
	}
}
