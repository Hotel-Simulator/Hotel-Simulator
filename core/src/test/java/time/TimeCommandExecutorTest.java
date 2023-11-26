package time;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;

public class TimeCommandExecutorTest {

	private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2020, 1, 13, 12, 0);
	private static final LocalDateTime DUE_DATE_TIME_LATER = LocalDateTime.of(2020, 1, 13, 12, 30);
	private TimeCommandExecutor executor;

	@BeforeEach
	public void setUp() throws NoSuchFieldException , IllegalAccessException {
		executor = TimeCommandExecutor.getInstance();
	}

	@AfterEach
	public void tearDown() throws NoSuchFieldException , IllegalAccessException {
		Field instanceField = TimeCommandExecutor.class.getDeclaredField("instance");
		instanceField.setAccessible(true);
		instanceField.set(null, null);
	}

	@Test
	public void testExecuteCommands_WithCommands() {
		// Given
		TimeCommand command1 = createMockTimeCommand();
		TimeCommand command2 = createMockLaterTimeCommand();

		executor.addCommand(command1);
		executor.addCommand(command2);

		// When
		executor.executeCommands(DUE_DATE_TIME);

		// Then
		verify(command1, times(1)).execute();
	}

	@Test
	public void testAddCommand() {
		// Given
		TimeCommand mockCommand = createMockTimeCommand();

		// When
		executor.addCommand(mockCommand);

		// Then
		PriorityQueue<TimeCommand> commands = getCommandsQueue(executor);
		assertTrue(commands.contains(mockCommand));
	}

	@Test
	public void testAddCommandWithoutSerialization() {
		// Given
		TimeCommand mockCommand = createMockTimeCommand();

		// When
		executor.addCommand(mockCommand, false);

		// Then
		PriorityQueue<TimeCommand> commands = getUnserializableCommandQueue(executor);
		assertTrue(commands.contains(mockCommand));
	}

	@Test
	public void testExecuteCommands_InCorrectOrder() {
		// Given
		TimeCommand command1 = createMockTimeCommand();
		TimeCommand command2 = createMockLaterTimeCommand();

		executor.addCommand(command1);
		executor.addCommand(command2);

		// When
		executor.executeCommands(DUE_DATE_TIME_LATER);

		// Then
		InOrder inOrder = inOrder(command1, command2);
		inOrder.verify(command1).execute();
		inOrder.verify(command2).execute();
	}

	private PriorityQueue<TimeCommand> getCommandsQueue(TimeCommandExecutor executor) {
		try {
			java.lang.reflect.Field field = TimeCommandExecutor.class.getDeclaredField("commandQueue");
			field.setAccessible(true);
			return (PriorityQueue<TimeCommand>) field.get(executor);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access 'commands' field", e);
		}
	}

	private PriorityQueue<TimeCommand> getUnserializableCommandQueue(TimeCommandExecutor executor) {
		try {
			java.lang.reflect.Field field = TimeCommandExecutor.class.getDeclaredField("unserializableCommandQueue");
			field.setAccessible(true);
			return (PriorityQueue<TimeCommand>) field.get(executor);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access 'commands' field", e);
		}
	}

	private TimeCommand createMockTimeCommand() {
		TimeCommand timeCommand = mock(TimeCommand.class);
		when(timeCommand.getDueDateTime()).thenReturn(DUE_DATE_TIME);
		when(timeCommand.compareTo(any())).thenReturn(-1);
		return timeCommand;
	}

	private TimeCommand createMockLaterTimeCommand() {
		TimeCommand timeCommand = mock(TimeCommand.class);
		when(timeCommand.getDueDateTime()).thenReturn(DUE_DATE_TIME_LATER);
		when(timeCommand.compareTo(any())).thenReturn(1);
		return timeCommand;
	}
}
