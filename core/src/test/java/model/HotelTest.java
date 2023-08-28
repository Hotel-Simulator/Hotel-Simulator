package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.Client;
import pl.agh.edu.model.client.ClientGroup;

public class HotelTest {

	@BeforeEach
	public void setUp() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@Test
	public void findRoomForClientGroupSuccessTest() {

		ClientGroup group = getClientGroup();

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		Room room = new Room(RoomRank.THREE, 2, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.setRentPrice(BigDecimal.valueOf(1000L));

		hotel.addRoomByRank(room);

		assertEquals(hotel.findRoomForClientGroup(group).get(), room);
	}

	@Test
	public void findRoomForClientGroupDirtyRoomTest() {

		ClientGroup group = getClientGroup();

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		Room room = new Room(RoomRank.THREE, 2, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.getRoomStates().setDirty(true);

		hotel.addRoomByRank(room);

		assertEquals(hotel.findRoomForClientGroup(group).get(), room);
	}

	@Test
	public void findRoomForClientGroupExpensiveRoomTest() {

		ClientGroup group = getClientGroup();

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		Room room = new Room(RoomRank.THREE, 2, BigDecimal.valueOf(3000), BigDecimal.valueOf(4000));

		hotel.addRoomByRank(room);

		assertTrue(hotel.findRoomForClientGroup(group).isEmpty());
	}

	@Test
	public void findRoomForClientGroupNoDesiredRoomRankTest() {

		ClientGroup group = getClientGroup();

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		Room room = new Room(RoomRank.ONE, 2, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

		hotel.addRoomByRank(room);

		assertTrue(hotel.findRoomForClientGroup(group).isEmpty());
	}

	@Test
	public void findRoomForClientGroupFaultyRoomTest() {

		ClientGroup group = getClientGroup();

		Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		Room room = new Room(RoomRank.THREE, 2, BigDecimal.valueOf(100), BigDecimal.valueOf(200));
		room.getRoomStates().setBeingUpgraded(true);

		hotel.addRoomByRank(room);

		assertTrue(hotel.findRoomForClientGroup(group).isEmpty());
	}

	private ClientGroup getClientGroup() {
		List<Client> clients = new ArrayList<>();
		clients.add(new Client(23, Sex.MALE, HotelVisitPurpose.BUSINESS_TRIP));
		clients.add(new Client(22, Sex.FEMALE, HotelVisitPurpose.BUSINESS_TRIP));

		return new ClientGroup.Builder()
				.hotelVisitPurpose(HotelVisitPurpose.BUSINESS_TRIP)
				.members(clients)
				.checkOutTime(LocalDateTime.now())
				.desiredPricePerNight(BigDecimal.valueOf(2000))
				.desiredRoomRank(RoomRank.THREE)
				.build();
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}
}
