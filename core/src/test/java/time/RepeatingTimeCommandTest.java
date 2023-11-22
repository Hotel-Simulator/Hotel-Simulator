package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;

public class RepeatingTimeCommandTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
	private static final Frequency FREQUENCY = EVERY_DAY;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testExecuteCommand() {
		// Given
		SerializableRunnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		boolean repeat = command.execute();

		// Then
		assertTrue(repeat);
		verify(runnable, times(1)).run();
	}

	@Test
	public void testRepeatCommand() {
		// Given
		SerializableRunnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		boolean repeat = command.execute();

		// Then
		assertTrue(repeat);
		verify(runnable, times(1)).run();
		assertEquals(command.getDueDateTime(), DUE_DATE_TIME.plusDays(1));
	}

	@Test
	public void testStop() {
		// Given
		SerializableRunnable runnable = createMockRunnable();
		RepeatingTimeCommand command = new RepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME);

		// When
		command.stop();
		boolean repeat = command.execute();

		// Then
		assertFalse(repeat);
		verify(runnable, times(0)).run();
	}

	private SerializableRunnable createMockRunnable() {
		return mock(SerializableRunnable.class);
	}
}
