package model.employee;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import pl.agh.edu.model.employee.Shift;

public class ShiftTest {

	@ParameterizedTest
	@MethodSource("shiftAndTimesProvider")
	void testLasts(Shift shift, LocalTime time, boolean expected) {
		// Given When
		boolean result = shift.lasts(time);

		// Then
		assertEquals(expected, result);
	}

	static Stream<Arguments> shiftAndTimesProvider() {
		return Stream.of(
				Arguments.of(Shift.MORNING, LocalTime.of(8, 0), true),
				Arguments.of(Shift.MORNING, LocalTime.of(15, 59), true),
				Arguments.of(Shift.MORNING, LocalTime.of(16, 0), false),
				Arguments.of(Shift.MORNING, LocalTime.of(7, 59), false),
				Arguments.of(Shift.EVENING, LocalTime.of(16, 0), true),
				Arguments.of(Shift.EVENING, LocalTime.of(23, 59), true),
				Arguments.of(Shift.EVENING, LocalTime.of(0, 0), false),
				Arguments.of(Shift.EVENING, LocalTime.of(15, 59), false),
				Arguments.of(Shift.NIGHT, LocalTime.of(0, 0), true),
				Arguments.of(Shift.NIGHT, LocalTime.of(7, 59), true),
				Arguments.of(Shift.NIGHT, LocalTime.of(8, 0), false),
				Arguments.of(Shift.NIGHT, LocalTime.of(23, 59), false));
	}

	@ParameterizedTest
	@EnumSource(Shift.class)
	void testNext(Shift shift) {
		Shift nextShift = shift.next();
		if (shift == Shift.MORNING) {
			assertSame(nextShift, Shift.EVENING);
		} else if (shift == Shift.EVENING) {
			assertSame(nextShift, Shift.NIGHT);
		} else if (shift == Shift.NIGHT) {
			assertSame(nextShift, Shift.MORNING);
		}
	}
}
