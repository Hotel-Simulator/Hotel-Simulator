package pl.agh.edu.management.room;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
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
	private final RoomPriceList roomPriceList;

	public RoomManager(List<Room> rooms) {
		this.rooms = rooms;
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.time = Time.getInstance();
		this.roomRankChangeTimes = new HashMap<>();
		this.roomPriceList = new RoomPriceList(JSONClientDataLoader.averagePricesPerNight);
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
				room -> room.capacity,
				() -> new EnumMap<>(RoomCapacity.class),
				Collectors.toList()));
	}

	public Optional<Room> findRoomForClientGroup(ClientGroup group) {
		return rooms.stream()
				.filter(room -> room.getRank() == group.getDesiredRoomRank())
				.filter(room -> room.capacity.value == group.getSize())
				.filter(room -> !room.roomState.isOccupied())
				.filter(room -> !room.roomState.isUnderRankChange())
				.filter(room -> roomPriceList.getPrice(room).compareTo(group.getDesiredPricePerNight()) < 1)
				.min(Comparator.comparing(room -> room.roomState.isDirty()));
	}

	public void changeRoomRank(Room room, RoomRank desiredRank) {
		if (desiredRank == room.getRank()) {
			throw new IllegalArgumentException("Desired roomRank must be other than current.");
		}
		room.roomState.setUnderRankChange(true);

		LocalDateTime upgradeTime = time.getTime().plus(JSONRoomDataLoader.roomRankChangeDuration.get(desiredRank));
		roomRankChangeTimes.put(room, upgradeTime);

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					room.roomState.setUnderRankChange(false);
					room.upgradeRank(desiredRank);
					roomRankChangeTimes.remove(room);
				},
				upgradeTime));
	}

	public Optional<LocalDateTime> findChangeRankTime(Room room) {
		return Optional.ofNullable(roomRankChangeTimes.get(room));
	}

	public boolean canChangeRoomRank(Room room) {
		return !room.roomState.isOccupied()
				&& !room.roomState.isUnderRankChange()
				&& !room.roomState.isFaulty()
				&& !room.roomState.isDirty();
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

}
