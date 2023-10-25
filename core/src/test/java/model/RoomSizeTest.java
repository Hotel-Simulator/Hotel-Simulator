package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.room.RoomSize.FAMILY;
import static pl.agh.edu.engine.room.RoomSize.SINGLE;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.engine.room.RoomSize;

public class RoomSizeTest {

	static Stream<Arguments> canAccommodateGuestsArguments() {
		return Stream.of(
				Arguments.of(SINGLE, 0, false),
				Arguments.of(SINGLE, 1, true),
				Arguments.of(SINGLE, 2, false),
				Arguments.of(DOUBLE, 2, true),
				Arguments.of(DOUBLE, 3, false),
				Arguments.of(FAMILY, 5, true),
				Arguments.of(FAMILY, 6, false));
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
				Arguments.of(1, Optional.of(SINGLE)),
				Arguments.of(2, Optional.of(DOUBLE)),
				Arguments.of(3, Optional.of(FAMILY)),
				Arguments.of(4, Optional.of(FAMILY)),
				Arguments.of(5, Optional.of(FAMILY)),
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
