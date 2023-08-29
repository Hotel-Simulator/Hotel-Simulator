package pl.agh.edu.management.room;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.json.data_loader.JSONRoomDataLoader;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.RoomPriceList;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class RoomManager {

	private final List<Room> rooms;
	private final TimeCommandExecutor timeCommandExecutor;
	private final Time time;
	private final Map<Room, LocalDateTime> roomRankChangeTimes;

	public RoomManager() {
		this.rooms = JSONHotelDataLoader.rooms;
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.time = Time.getInstance();
		this.roomRankChangeTimes = new HashMap<>();
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public EnumMap<RoomRank, List<Room>> getRoomsByRank() {
		return rooms.stream().collect(Collectors.groupingBy(
				Room::getRank,
				() -> new EnumMap<>(RoomRank.class),
				Collectors.toList()));
	}

	public Map<RoomCapacity, List<Room>> getRoomsByCapacity() {
		return rooms.stream().collect(Collectors.groupingBy(
				Room::getCapacity,
				() -> new EnumMap<>(RoomCapacity.class),
				Collectors.toList()));
	}

	public Room findRoomForClientGroup(ClientGroup group) {
		for (Room room : getRoomsByRank().get(group.getDesiredRoomRank())) {
			if (room.getState().equals(RoomState.EMPTY) && RoomPriceList.getPrice(room).compareTo(group.getDesiredPricePerNight()) < 1) {
				return room;
			}
		}
		return null;
	}

	public void changeRoomRank(Room room, RoomRank desiredRank) {
		if (desiredRank == room.getRank()) {
			throw new IllegalArgumentException("Desired roomRank must be other than current.");
		}
		room.setState(RoomState.UPGRADING); // todo zmienic

		LocalDateTime upgradeTime = time.getTime().plus(JSONRoomDataLoader.roomRankChangeDuration.get(desiredRank));
		roomRankChangeTimes.put(room, upgradeTime);

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					// room.setState(); koniec upgradowania
					room.upgradeRank(desiredRank);
					roomRankChangeTimes.remove(room);
				},
				upgradeTime));
	}

	public Optional<LocalDateTime> findUpgradeTime(Room room) {
		return Optional.ofNullable(roomRankChangeTimes.get(room));
	}

	public boolean canChangeRoomRank(Room room) {
		return false; // todo !isOccupied() && !isBroken() && !isBeingUpgraded
	}

}
