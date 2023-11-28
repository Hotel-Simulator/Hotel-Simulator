package pl.agh.edu.engine.room;

import static pl.agh.edu.engine.client.visit_history.VisitResult.ALL_ROOMS_OF_WANTED_SIZE_AND_TYPE_CURRENTLY_OCCUPIED;
import static pl.agh.edu.engine.client.visit_history.VisitResult.GOT_ROOM;
import static pl.agh.edu.engine.client.visit_history.VisitResult.HOTEL_DOES_NOT_OFFER_ANY_ROOMS;
import static pl.agh.edu.engine.client.visit_history.VisitResult.HOTEL_DOES_NOT_OFFER_ROOMS_OF_WANTED_SIZE_AND_TYPE;
import static pl.agh.edu.engine.client.visit_history.VisitResult.PRICE_TO_HIGH;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
			visitResult = HOTEL_DOES_NOT_OFFER_ANY_ROOMS;
		}
	}

	private RoomFilter hotelOfferRoomsOfWantedSizeAndTypeFilter() {
		return filter(
				room -> room.getRank() == clientGroup.getDesiredRoomRank()
						&& room.size.canAccommodateGuests(clientGroup.getSize()),
				HOTEL_DOES_NOT_OFFER_ROOMS_OF_WANTED_SIZE_AND_TYPE);
	}

	private RoomFilter roomsAreNotOccupiedFilter() {
		return filter(
				room -> !room.roomState.isOccupied(),
				ALL_ROOMS_OF_WANTED_SIZE_AND_TYPE_CURRENTLY_OCCUPIED);
	}

	private RoomFilter roomsAreAffordableFilter() {
		return filter(
				room -> roomPricePerNight.getPrice(room).compareTo(clientGroup.getDesiredPricePerNight()) < 1,
				PRICE_TO_HIGH);
	}

	private RoomFilter filter(Function<Room, Boolean> filterFunction, VisitResult visitResult) {
		List<Room> filteredRooms = rooms.stream()
				.filter(filterFunction::apply)
				.toList();

		if (rooms.size() != 0 && filteredRooms.size() == 0) {
			this.visitResult = visitResult;
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
				.roomsAreNotOccupiedFilter()
				.roomsAreAffordableFilter()
				.getRoom();

		return Pair.of(optionalRoom, visitResult);
	}

}
