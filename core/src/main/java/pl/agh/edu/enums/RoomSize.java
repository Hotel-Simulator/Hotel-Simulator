package pl.agh.edu.enums;

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

	public static Optional<RoomSize> getSmallestAvailableRoomSize(int numberOfGuests) {
		return switch (numberOfGuests) {
		case 1 -> Optional.of(RoomSize.SINGLE);
		case 2 -> Optional.of(RoomSize.DOUBLE);
		case 3, 4, 5 -> Optional.of(RoomSize.FAMILY);
		default -> Optional.empty();
		};
	}

}
