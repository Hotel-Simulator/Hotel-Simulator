package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.RoomState;
import pl.agh.edu.model.client.ClientGroup;

public class HotelTest {

	@Mock
	private Room mockRoom;

	@Mock
	private ClientGroup group;

	@BeforeEach
	public void setUp() throws ReflectiveOperationException {
		MockitoAnnotations.initMocks(this);
		changeJSONPath();
		mockRoom = mock(Room.class);
	}

	@Test
	public void findRoomForClientGroupTest_Success() {
		when(mockRoom.getRank()).thenReturn(RoomRank.THREE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(1000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertTrue(foundRoom.isPresent());
		assertEquals(mockRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_DirtyRoom() {
		when(mockRoom.getRank()).thenReturn(RoomRank.THREE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(1000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());
		mockRoom.getRoomStates().setDirty(true);

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertTrue(foundRoom.isPresent());
		assertEquals(mockRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_TooExpensiveRoom() {
		when(mockRoom.getRank()).thenReturn(RoomRank.THREE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(3000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());


		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_NoDesiredRoomRank() {
		when(mockRoom.getRank()).thenReturn(RoomRank.ONE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(1000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());


		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_FaultyRoom() {
		when(mockRoom.getRank()).thenReturn(RoomRank.THREE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(1000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());
		mockRoom.getRoomStates().setFaulty(true);

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertEquals(foundRoom.get(), mockRoom);
	}

	@Test
	public void findRoomForClientGroupTest_UpgradingRoom() {
		when(mockRoom.getRank()).thenReturn(RoomRank.THREE);
		when(mockRoom.getRentPrice()).thenReturn(BigDecimal.valueOf(1000L));
		when(mockRoom.getRoomStates()).thenReturn(new RoomState());
		mockRoom.getRoomStates().setBeingUpgraded(true);

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		hotel.addRoomByRank(mockRoom);

		getClientGroup();
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		assertTrue(foundRoom.isEmpty());
	}

	private void getClientGroup() {
		when(group.getDesiredRoomRank()).thenReturn(RoomRank.THREE);
		when(group.getDesiredPricePerNight()).thenReturn(BigDecimal.valueOf(2000));
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
