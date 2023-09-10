package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.enums.RoomSize;

public class RoomSizeTest {

	static Stream<Arguments> canAccommodateGuestsArguments() {
		return Stream.of(
				Arguments.of(RoomSize.SINGLE, 0, false),
				Arguments.of(RoomSize.SINGLE, 1, true),
				Arguments.of(RoomSize.SINGLE, 2, false),
				Arguments.of(RoomSize.DOUBLE, 2, true),
				Arguments.of(RoomSize.DOUBLE, 3, false),
				Arguments.of(RoomSize.FAMILY, 5, true),
				Arguments.of(RoomSize.FAMILY, 6, false));
	}

	@ParameterizedTest()
	@MethodSource("canAccommodateGuestsArguments")
	void canAccommodateGuestsTest(RoomSize roomSize, int numberOfGuests, boolean expected) {
		// When
		boolean result = roomSize.canAccommodateGuests(numberOfGuests);

		// Then
		assertEquals(expected, result);
	}

	private static Stream<Arguments> getSmallestAvailableRoomSizeArguments() {
		return Stream.of(
				Arguments.of(0, Optional.empty()),
				Arguments.of(1, Optional.of(RoomSize.SINGLE)),
				Arguments.of(2, Optional.of(RoomSize.DOUBLE)),
				Arguments.of(3, Optional.of(RoomSize.FAMILY)),
				Arguments.of(4, Optional.of(RoomSize.FAMILY)),
				Arguments.of(5, Optional.of(RoomSize.FAMILY)),
				Arguments.of(6, Optional.empty()));
	}

	@ParameterizedTest
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@MethodSource("getSmallestAvailableRoomSizeArguments")
	void getSmallestAvailableRoomSizeTest(int clientGroupSize, Optional<RoomSize> expectedRoomSize) {
		// When
		Optional<RoomSize> result = RoomSize.getSmallestAvailableRoomSize(clientGroupSize);

		// Then
		assertEquals(expectedRoomSize, result);
	}
}
