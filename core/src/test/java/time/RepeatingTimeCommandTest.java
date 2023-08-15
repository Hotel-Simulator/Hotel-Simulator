package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.time_command.RepeatingTimeCommand;

public class RepeatingTimeCommandTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
	private static final Frequency FREQUENCY = Frequency.EVERY_DAY;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testExecuteCommand() {
		// Given
		Runnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		command.execute();

		// Then
		verify(runnable, times(1)).run();
	}

	@Test
	public void testRepeatCommand() {
		// Given
		Runnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		command.execute();

		// Then
		verify(runnable, times(1)).run();
		assertEquals(command.getDueDateTime(), DUE_DATE_TIME.plusDays(1));
	}

	@Test
	public void testStop() {
		// Given
		Runnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		command.stop();
		command.execute();

		// Then
		verify(runnable, times(0)).run();
	}

	private Runnable createMockRunnable() {
		return mock(Runnable.class);
	}
}
