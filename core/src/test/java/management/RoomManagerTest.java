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

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.management.room.RoomManager;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class RoomManagerTest {

	private RoomManager roomManager;
	private List<Room> rooms;

	private ClientGroup clientGroup;

	@BeforeAll
	public static void setUpClass() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	public void setUp() {
		rooms = new ArrayList<>();
		rooms.add(new Room(RoomRank.ONE, RoomCapacity.ONE));
		rooms.add(new Room(RoomRank.FIVE, RoomCapacity.FIVE));

		roomManager = new RoomManager(rooms);

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
	public void canChangeRoomRank_BeingUpgraded() {
		// Given
		Room room = rooms.get(0);
		room.roomState.setBeingUpgraded(true);

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
		assertTrue(roomsByRank.containsKey(RoomRank.ONE));
		assertEquals(1, roomsByRank.get(RoomRank.ONE).size());
		assertTrue(roomsByRank.containsKey(RoomRank.FIVE));
		assertEquals(1, roomsByRank.get(RoomRank.FIVE).size());

	}

	@Test
	public void getRoomsByCapacity() {
		Map<RoomCapacity, List<Room>> roomsByCapacity = roomManager.getRoomsByCapacity();

		assertNotNull(roomsByCapacity);
		assertEquals(2, roomsByCapacity.size());
		assertTrue(roomsByCapacity.containsKey(RoomCapacity.ONE));
		assertEquals(1, roomsByCapacity.get(RoomCapacity.ONE).size());
		assertTrue(roomsByCapacity.containsKey(RoomCapacity.FIVE));
		assertEquals(1, roomsByCapacity.get(RoomCapacity.FIVE).size());

	}

	@Test
	public void findRoomForClientGroupTest_Success() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
		when(clientGroup.getSize()).thenReturn(3);
		when(clientGroup.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(1000));

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(room, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_DirtyRoom() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		room.roomState.setDirty(true);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
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
		Room dirtyRoom = new Room(RoomRank.THREE, RoomCapacity.THREE);
		Room cleanRoom = new Room(RoomRank.THREE, RoomCapacity.THREE);

		dirtyRoom.roomState.setDirty(true);
		roomManager.addRoom(dirtyRoom);
		roomManager.addRoom(cleanRoom);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
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
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
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
		Room room = new Room(RoomRank.ONE, RoomCapacity.THREE);
		roomManager.addRoom(room);

		when(clientGroup.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
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
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		room.roomState.setFaulty(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	@Test
	public void findRoomForClientGroupTest_UpgradingRoom() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
		room.roomState.setBeingUpgraded(true);
		roomManager.addRoom(room);

		// When
		Optional<Room> foundRoom = roomManager.findRoomForClientGroup(clientGroup);

		// Then
		assertFalse(foundRoom.isPresent());
	}

	@Test
	public void findRoomForClientGroupTest_BuildingRoom() {
		// Given
		Room room = new Room(RoomRank.THREE, RoomCapacity.THREE);
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
