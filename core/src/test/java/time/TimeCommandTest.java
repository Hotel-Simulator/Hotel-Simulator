package time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.engine.time.command.TimeCommand;

public class TimeCommandTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
	private static final LocalDateTime OTHER_DUE_DATE_TIME = LocalDateTime.of(2023, 8, 14, 12, 0);
	private static final Runnable MOCK_RUNNABLE = mock(Runnable.class);

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testExecuteCommand() {
		// Given
		TimeCommand command = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);

		// When
		command.execute();

		// Then
		verify(MOCK_RUNNABLE, times(1)).run();
	}

	@Test
	public void testGetDueDateTime() {
		// When
		TimeCommand command = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);

		// Then
		assertEquals(DUE_DATE_TIME, command.getDueDateTime());
	}

	@Test
	public void testCompareTo_SameDueDateTime() {
		// When
		TimeCommand command1 = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);
		TimeCommand command2 = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);

		// Then
		assertTrue(command1.compareTo(command2) < 0);
	}

	@Test
	public void testCompareTo_DifferentDueDateTime() {
		// When
		TimeCommand command1 = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);
		TimeCommand command2 = new TimeCommand(MOCK_RUNNABLE, OTHER_DUE_DATE_TIME);

		// Then
		assertTrue(command1.compareTo(command2) < 0);
	}

	@Test
	public void testCompareTo_SameDueDateTimeDifferentCreatedDateTime() {
		// When
		TimeCommand command1 = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME);
		TimeCommand command2 = new TimeCommand(MOCK_RUNNABLE, DUE_DATE_TIME.plusSeconds(1));

		// Then
		assertTrue(command1.compareTo(command2) < 0);
	}
}
