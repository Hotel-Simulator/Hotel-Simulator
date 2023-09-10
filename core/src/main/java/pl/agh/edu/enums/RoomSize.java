package pl.agh.edu.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RoomSize {
	SINGLE(1),
	DOUBLE(2),
	FAMILY(5);

	private final int maxNumberOfGuests;

	RoomSize(int maxNumberOfGuests) {
		this.maxNumberOfGuests = maxNumberOfGuests;
	}

	public boolean canAccommodateGuests(int numberOfGuests) {
		return maxNumberOfGuests >= numberOfGuests && numberOfGuests > 0;
	}

	public static Optional<RoomSize> getSmallestAvailableRoomSize(int clientGroupSize) {
		return Arrays.stream(RoomSize.values())
				.filter(roomSize -> roomSize.canAccommodateGuests(clientGroupSize))
				.min(RoomSize::compareTo);
	}

}
