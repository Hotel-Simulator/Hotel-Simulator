package pl.agh.edu.engine.room;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.agh.edu.data.loader.JSONClientDataLoader;
import pl.agh.edu.data.loader.JSONRoomDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.Pair;

public class RoomManager {

	private final List<Room> rooms;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final Time time = Time.getInstance();
	private final Map<Room, LocalDateTime> roomRankChangeTimes = new HashMap<>();
	private final Map<Room, LocalDateTime> roomBuildingTimes = new HashMap<>();
	private final RoomPricePerNight roomPricePerNight = new RoomPricePerNight(JSONClientDataLoader.averagePricesPerNight);
	private final BankAccountHandler bankAccountHandler;

	public RoomManager(List<Room> initialRooms, BankAccountHandler bankAccountHandler) {
		this.rooms = initialRooms;
		this.bankAccountHandler = bankAccountHandler;
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

	public Map<RoomSize, List<Room>> getRoomsBySize() {
		return rooms.stream().collect(Collectors.groupingBy(
				room -> room.size,
				() -> new EnumMap<>(RoomSize.class),
				Collectors.toList()));
	}

	public Optional<Room> findRoomForClientGroup(ClientGroup group) {
		return rooms.stream()
				.filter(room -> room.getRank() == group.getDesiredRoomRank())
				.filter(room -> room.size.canAccommodateGuests(group.getSize()))
				.filter(room -> !room.roomState.isOccupied())
				.filter(room -> !room.roomState.isUnderRankChange())
				.filter(room -> !room.roomState.isBeingBuild())
				.filter(room -> roomPricePerNight.getPrice(room).compareTo(group.getDesiredPricePerNight()) < 1)
				.sorted(Comparator.comparing(roomPricePerNight::getPrice))
				.min(Comparator.comparing(room -> room.roomState.isDirty()));
	}

	private double roomTimeMultiplier(Room room) {
		return (room.getRank().ordinal() + 1) + (room.size.ordinal() + 1) / 2.;
	}

	public void changeRoomRank(Room room, RoomRank desiredRank) {
		if (desiredRank == room.getRank()) {
			throw new IllegalArgumentException("Desired roomRank must be other than current.");
		}
		BigDecimal changeCost = getChangeCost(room.getRank(), desiredRank, room.size);
		if (changeCost.signum() > 0) {
			bankAccountHandler.registerExpense(changeCost);
		} else {
			bankAccountHandler.registerIncome(changeCost.negate());
		}
		room.roomState.setUnderRankChange(true);

		LocalDateTime upgradeTime = time.getTime().plusHours(
				(long) (JSONRoomDataLoader.roomRankChangeDuration.toHours() * roomTimeMultiplier(room)));
		roomRankChangeTimes.put(room, upgradeTime);

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					room.roomState.setUnderRankChange(false);
					room.changeRank(desiredRank);
					roomRankChangeTimes.remove(room);
				},
				upgradeTime));
	}

	private BigDecimal getChangeCost(RoomRank currentRank, RoomRank desiredRank, RoomSize size) {
		return JSONRoomDataLoader.roomBuildingCosts.get(Pair.of(desiredRank, size))
				.subtract(JSONRoomDataLoader.roomBuildingCosts.get(Pair.of(currentRank, size)))
				.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_EVEN);
	}

	public Optional<LocalDateTime> findChangeRankTime(Room room) {
		return Optional.ofNullable(roomRankChangeTimes.get(room));
	}

	public Optional<LocalDateTime> findBuildTime(Room room) {
		return Optional.ofNullable(roomBuildingTimes.get(room));
	}

	public boolean canChangeRoomRank(Room room) {
		return !room.roomState.isOccupied()
				&& !room.roomState.isUnderRankChange()
				&& !room.roomState.isFaulty()
				&& !room.roomState.isDirty()
				&& !room.roomState.isBeingBuild();
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public void buildRoom(RoomRank roomRank, RoomSize roomSize) {
		Room buildRoom = new Room(roomRank, roomSize);
		buildRoom.roomState.setBeingBuild(true);
		rooms.add(buildRoom);
		bankAccountHandler.registerExpense(JSONRoomDataLoader.roomBuildingCosts.get(Pair.of(roomRank, roomSize)));

		LocalDateTime buildTime = time.getTime().plusHours(
				(long) (JSONRoomDataLoader.roomBuildingDuration.toHours() * roomTimeMultiplier(buildRoom)));
		roomBuildingTimes.put(buildRoom, buildTime);

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> {
					buildRoom.roomState.setBeingBuild(false);
					roomBuildingTimes.remove(buildRoom);
				}, buildTime));
	}

	public RoomPricePerNight getRoomPriceList() {
		return roomPricePerNight;
	}
}