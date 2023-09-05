package pl.agh.edu.enums;

import java.util.Arrays;

public enum RoomSize {
	SINGLE(1),
	DOUBLE(2),
	FAMILY(3, 4, 5);

	private final int[] acceptableNumberOfGuests;

	RoomSize(int... acceptableNumberOfGuests) {
		this.acceptableNumberOfGuests = acceptableNumberOfGuests;
	}

	public boolean canAccommodateGuests(int numberOfGuests) {
		return Arrays.stream(acceptableNumberOfGuests)
				.anyMatch(i -> i == numberOfGuests);
	}

	public static RoomSize getRoomSize(int clientGroupSize) {
		return Arrays.stream(RoomSize.values())
				.filter(roomSize -> roomSize.canAccommodateGuests(clientGroupSize))
				.findFirst()
				.orElseThrow();
	}

}
