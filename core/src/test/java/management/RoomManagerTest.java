package management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.management.room.RoomManager;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class RoomManagerTest {

	private RoomManager roomManager;
	private List<Room> rooms;

	private ClientGroup clientGroup;
	@Mock
	BankAccountHandler bankAccountHandler;

	@BeforeAll
	public static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	public void setUp() {
		rooms = new ArrayList<>();
		rooms.add(new Room(RoomRank.STANDARD, RoomSize.SINGLE));
		rooms.add(new Room(RoomRank.DELUXE, RoomSize.DOUBLE));

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
		assertTrue(roomsByRank.containsKey(RoomRank.STANDARD));
		assertEquals(1, roomsByRank.get(RoomRank.STANDARD).size());
		assertTrue(roomsByRank.containsKey(RoomRank.DELUXE));
		assertEquals(1, roomsByRank.get(RoomRank.DELUXE).size());

	}

	@Test
	public void getRoomsBySize() {
		Map<RoomSize, List<Room>> roomsBySize = roomManager.getRoomsBySize();

		assertNotNull(roomsBySize);
		assertEquals(2, roomsBySize.size());
		assertTrue(roomsBySize.containsKey(RoomSize.SINGLE));
		assertEquals(1, roomsBySize.get(RoomSize.SINGLE).size());
		assertTrue(roomsBySize.containsKey(RoomSize.DOUBLE));
		assertEquals(1, roomsBySize.get(RoomSize.DOUBLE).size());

	}

	@Test
	public void findRoomForClientGroupTest_Success() {
		// Given
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.STANDARD);
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
		Room room1 = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		Room room2 = new Room(RoomRank.STANDARD, RoomSize.DOUBLE);
		roomManager.addRoom(room1);
		roomManager.addRoom(room2);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.STANDARD);
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
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		room.roomState.setDirty(true);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.STANDARD);
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
		Room dirtyRoom = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		Room cleanRoom = new Room(RoomRank.STANDARD, RoomSize.FAMILY);

		dirtyRoom.roomState.setDirty(true);
		roomManager.addRoom(dirtyRoom);
		roomManager.addRoom(cleanRoom);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.STANDARD);
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
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.STANDARD);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.ZERO);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_NoDesiredRoomRank() {
		// Given
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.ECONOMIC);
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
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		room.roomState.setFaulty(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	@Test
	public void findRoomForClientGroupTest_rankChangeRoom() {
		// Given
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
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
		Room room = new Room(RoomRank.STANDARD, RoomSize.FAMILY);
		room.roomState.setBeingBuild(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
