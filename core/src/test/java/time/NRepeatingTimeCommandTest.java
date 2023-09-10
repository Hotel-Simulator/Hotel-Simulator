package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.time_command.NRepeatingTimeCommand;

public class NRepeatingTimeCommandTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
	private static final Frequency FREQUENCY = Frequency.EVERY_DAY;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCounterDecrease() {
		// Given
		Runnable runnable = createMockRunnable();
		int N = 3;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N);

		// When
		command.execute();

		// Then
		verify(runnable, times(1)).run();
		assertEquals(N - 1, command.getCounter());
	}

	@Test
	public void testCommandStops_ExecutedNTimes() {
		// Given
		Runnable runnable = createMockRunnable();
		int N = 1;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N);

		// When
		command.execute();
		command.execute();

		// Then
		verify(runnable, times(1)).run();
	}

	private Runnable createMockRunnable() {
		return mock(Runnable.class);
	}
}
