package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;

public class NRepeatingTimeCommandTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
	private static final Frequency FREQUENCY = EVERY_DAY;
	@Mock
	private static SerializableRunnable runnable;
	@Mock
	private static SerializableRunnable runnableAfterLast;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCounterDecrease() {
		// Given
		long N = 3;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N);

		// When
		command.execute();

		// Then
		verify(runnable, times(1)).run();
		assertEquals(N - 1, command.getCounter());
	}

	@Test
	public void testLastExecutedTime() {
		// Given
		long N = 1;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N);

		// When
		command.execute();
		command.execute();

		// Then
		verify(runnable, times(1)).run();
		assertEquals(0, command.getCounter());
	}

	@Test
	public void testLastTwoExecutedTimes() {
		// Given
		long N = 1;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N, runnableAfterLast);

		// When
		command.execute();
		command.execute();

		// Then
		verify(runnable, times(1)).run();
		verify(runnableAfterLast, times(1)).run();
	}

	@Test
	public void testLastNExecutedTimes() {
		// Given
		long N = 10;
		NRepeatingTimeCommand command = new NRepeatingTimeCommand(FREQUENCY, runnable, DUE_DATE_TIME, N, runnableAfterLast);

		// When
		command.execute();
		command.execute();

		// Then
		verify(runnable, times(2)).run();
		verify(runnableAfterLast, times(0)).run();
	}
}
