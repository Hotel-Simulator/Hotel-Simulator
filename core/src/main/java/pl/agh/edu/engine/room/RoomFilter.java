package pl.agh.edu.engine.room;

import static pl.agh.edu.engine.client.visit_history.VisitResult.GOT_ROOM;
import static pl.agh.edu.engine.client.visit_history.VisitResult.NO_ROOM_OF_WANTED_RANK;
import static pl.agh.edu.engine.client.visit_history.VisitResult.NO_ROOM_OF_WANTED_RANK_AND_SIZE;
import static pl.agh.edu.engine.client.visit_history.VisitResult.NO_ROOM_OF_WANTED_SIZE;
import static pl.agh.edu.engine.client.visit_history.VisitResult.PRICE_TO_HIGH;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.visit_history.VisitResult;
import pl.agh.edu.utils.Pair;

public class RoomFilter {
	private List<Room> rooms;
	private final ClientGroup clientGroup;
	private final RoomPricePerNight roomPricePerNight;
	private VisitResult visitResult = GOT_ROOM;

	private final Comparator<Room> roomComparator;

	public RoomFilter(List<Room> rooms, ClientGroup clientGroup, RoomPricePerNight roomPricePerNight) {
		this.rooms = rooms.stream()
				.filter(room -> !room.roomState.isUnderRankChange())
				.filter(room -> !room.roomState.isBeingBuild())
				.filter(room -> !room.roomState.isOccupied())
				.toList();

		this.clientGroup = clientGroup;
		this.roomPricePerNight = roomPricePerNight;

		this.roomComparator = (o1, o2) -> {
			int broken = Boolean.compare(o1.roomState.isFaulty(), o2.roomState.isFaulty());
			if (broken != 0) {
				return broken;
			}
			int dirty = Boolean.compare(o1.roomState.isDirty(), o2.roomState.isDirty());
			if (dirty != 0) {
				return dirty;
			}
			return roomPricePerNight.getPrice(o1).compareTo(roomPricePerNight.getPrice(o2));
		};

		if (rooms.size() == 0) {
			visitResult = NO_ROOM_OF_WANTED_RANK_AND_SIZE;
		}
	}

	private RoomFilter hotelOfferRoomsOfWantedSizeAndTypeFilter() {
		List<Room> filteredRoomsByRank = rooms.stream()
				.filter(room -> room.getRank() == clientGroup.getDesiredRoomRank())
				.toList();

		List<Room> filteredRoomsBySize = rooms.stream()
				.filter(room -> room.size.canAccommodateGuests(clientGroup.getSize()))
				.toList();

		if (rooms.size() != 0) {
			if (filteredRoomsByRank.size() == 0 && filteredRoomsBySize.size() == 0) {
				this.visitResult = NO_ROOM_OF_WANTED_RANK_AND_SIZE;
			} else if (filteredRoomsByRank.size() == 0) {
				this.visitResult = NO_ROOM_OF_WANTED_RANK;
			} else if (filteredRoomsBySize.size() == 0)
				this.visitResult = NO_ROOM_OF_WANTED_SIZE;
		}

		rooms = filteredRoomsByRank.stream()
				.filter(filteredRoomsBySize::contains)
				.collect(Collectors.toList());

		return this;
	}

	private RoomFilter roomsAreAffordableFilter() {
		List<Room> filteredRooms = rooms.stream()
				.filter(room -> roomPricePerNight.getPrice(room).compareTo(clientGroup.getDesiredPricePerNight()) < 1)
				.toList();

		if (rooms.size() != 0 && filteredRooms.size() == 0) {
			this.visitResult = PRICE_TO_HIGH;
		}

		rooms = filteredRooms;

		return this;
	}

	private Optional<Room> getRoom() {
		return rooms.stream().min(roomComparator);
	}

	public Pair<Optional<Room>, VisitResult> findRoom() {
		Optional<Room> optionalRoom = this
				.hotelOfferRoomsOfWantedSizeAndTypeFilter()
				.roomsAreAffordableFilter()
				.getRoom();

		return Pair.of(optionalRoom, visitResult);
	}

}
