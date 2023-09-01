package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;

public class HotelTest {

	private final ClientGroup group = mock(ClientGroup.class);

	private Room mockRoom;

	private Hotel hotel;

	@BeforeAll
	static void setUp() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@BeforeEach
	public void setVariables() {
		hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		setClientGroup();
		mockRoom = new Room(RoomRank.THREE, 5);
		mockRoom.setRentPrice(BigDecimal.valueOf(1000L));
	}

	@Test
	public void findRoomForClientGroupTest_Success() {
		// Given
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(mockRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_DirtyRoom() {
		// Given
		mockRoom.roomState.setDirty(true);
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals(mockRoom, foundRoom.get());
	}

	@Test
	public void findRoomForClientGroupTest_TooExpensiveRoom() {
		// Given
		mockRoom.setRentPrice(BigDecimal.valueOf(3000L));
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_NoDesiredRoomRank() {
		// Given
		mockRoom.setRank(RoomRank.ONE);
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	@Test
	public void findRoomForClientGroupTest_FaultyRoom() {
		// Given
		mockRoom.roomState.setFaulty(true);
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertEquals(foundRoom.get(), mockRoom);
	}

	@Test
	public void findRoomForClientGroupTest_UpgradingRoom() {
		// Given
		mockRoom.roomState.setBeingUpgraded(true);
		hotel.addRoomByRank(mockRoom);

		// When
		Optional<Room> foundRoom = hotel.findRoomForClientGroup(group);

		// Then
		assertTrue(foundRoom.isEmpty());
	}

	private void setClientGroup() {
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
