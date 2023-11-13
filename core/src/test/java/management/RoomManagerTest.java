package management;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.agh.edu.engine.room.RoomRank.DELUXE;
import static pl.agh.edu.engine.room.RoomRank.ECONOMIC;
import static pl.agh.edu.engine.room.RoomRank.STANDARD;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.room.RoomSize.FAMILY;
import static pl.agh.edu.engine.room.RoomSize.SINGLE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;

public class RoomManagerTest {

	private RoomManager roomManager;
	private List<Room> rooms;

	private ClientGroup clientGroup;
	@Mock
	BankAccountHandler bankAccountHandler;

	@BeforeEach
	public void setUp() {
		rooms = new ArrayList<>();
		rooms.add(new Room(STANDARD, SINGLE));
		rooms.add(new Room(DELUXE, DOUBLE));

		roomManager = new RoomManager(rooms, bankAccountHandler);

		clientGroup = mock(ClientGroup.class);

	}

	@Test
	public void canChangeRoomRank() {
		// Given
		Room room = rooms.get(0);

		assertTrue(roomManager.canChangeRoomRank(room));
	}

	@Test
	public void canChangeRoomRank_Occupied() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setOccupied(true);

		// When
		boolean canChange = roomManager.canChangeRoomRank(room);

		// Then
		assertFalse(canChange);
	}

	@Test
	public void canChangeRoomRank_UnderRankChange() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setUnderRankChange(true);

		// When
		boolean canChange = roomManager.canChangeRoomRank(room);

		// Then
		assertFalse(canChange);
	}

	@Test
	public void canChangeRoomRank_Faulty() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setFaulty(true);

		// When
		boolean canChange = roomManager.canChangeRoomRank(room);

		// Then
		assertFalse(canChange);
	}

	@Test
	public void canChangeRoomRank_Dirty() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setDirty(true);

		// When
		boolean canChange = roomManager.canChangeRoomRank(room);

		// Then
		assertFalse(canChange);
	}

	@Test
	public void canChangeRoomRank_BeingBuild() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setBeingBuild(true);

		// When
		boolean canChange = roomManager.canChangeRoomRank(room);

		// Then
		assertFalse(canChange);
	}

	@Test
	public void getRoomsByRank() {
		EnumMap<RoomRank, List<Room>> roomsByRank = roomManager.getRoomsByRank();

		assertNotNull(roomsByRank);
		assertEquals(2, roomsByRank.size());
		assertTrue(roomsByRank.containsKey(STANDARD));
		assertEquals(1, roomsByRank.get(STANDARD).size());
		assertTrue(roomsByRank.containsKey(DELUXE));
		assertEquals(1, roomsByRank.get(DELUXE).size());

	}

	@Test
	public void getRoomsBySize() {
		Map<RoomSize, List<Room>> roomsBySize = roomManager.getRoomsBySize();

		assertNotNull(roomsBySize);
		assertEquals(2, roomsBySize.size());
		assertTrue(roomsBySize.containsKey(SINGLE));
		assertEquals(1, roomsBySize.get(SINGLE).size());
		assertTrue(roomsBySize.containsKey(DOUBLE));
		assertEquals(1, roomsBySize.get(DOUBLE).size());

	}

	@Test
	public void findRoomForClientGroupTest_Success() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(room, foundRoom.get());
	}

	@Test
	public void findTheCheapestRoomForClientGroupTest() {
		// Given
		Room room1 = new Room(STANDARD, FAMILY);
		Room room2 = new Room(STANDARD, DOUBLE);
		roomManager.addRoom(room1);
		roomManager.addRoom(room2);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(2);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(room2, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_DirtyRoom() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		room.roomState.setDirty(true);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(room, foundRoom.get());
	}

	@Test
	public void shouldReturnCleanRoom_DirtyRoomPossible() {
		// Given
		Room dirtyRoom = new Room(STANDARD, FAMILY);
		Room cleanRoom = new Room(STANDARD, FAMILY);

		dirtyRoom.roomState.setDirty(true);
		roomManager.addRoom(dirtyRoom);
		roomManager.addRoom(cleanRoom);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(cleanRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_TooExpensiveRoom() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(ZERO);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_NoDesiredRoomRank() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(ECONOMIC);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_FaultyRoom() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		room.roomState.setFaulty(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	@Test
	public void shouldReturnCleanRoom_FaultyRoomPossible() {
		// Given
		Room faultyRoom = new Room(STANDARD, FAMILY);
		Room cleanRoom = new Room(STANDARD, FAMILY);

		faultyRoom.roomState.setFaulty(true);
		roomManager.addRoom(faultyRoom);
		roomManager.addRoom(cleanRoom);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(cleanRoom, foundRoom.get());
	}

	@Test
	public void shouldReturnDirtyRoom_FaultyRoomPossible() {
		// Given
		Room faultyRoom = new Room(STANDARD, FAMILY);
		Room dirtyRoom = new Room(STANDARD, FAMILY);

		faultyRoom.roomState.setFaulty(true);
		dirtyRoom.roomState.setDirty(true);
		roomManager.addRoom(faultyRoom);
		roomManager.addRoom(dirtyRoom);

		when(clientGroup.getDesiredRoomRank()).thenReturn(STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(dirtyRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_rankChangeRoom() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		room.roomState.setUnderRankChange(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	@Test
	public void findRoomForClientGroupTest_BuildingRoom() {
		// Given
		Room room = new Room(STANDARD, FAMILY);
		room.roomState.setBeingBuild(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}
}
