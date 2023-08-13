package time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockitoAnnotations;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class TimeCommandExecutorTest {

    private static final LocalDateTime DUE_DATE_TIME = LocalDateTime.of(2023, 8, 13, 12, 0);
    private static final LocalDateTime DUE_DATE_TIME_LATER = LocalDateTime.of(2023, 8, 13, 12, 30);
    private TimeCommandExecutor executor;

    @BeforeEach
    public void setUp() {
        executor = TimeCommandExecutor.getInstance();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCommand() {
        // Given
        TimeCommand mockCommand = mock(TimeCommand.class);

        // When
        executor.addCommand(mockCommand);

        // Then
        PriorityQueue<TimeCommand> commands = getCommandsQueue(executor);
        assertTrue(commands.contains(mockCommand));
    }
    @Test
    public void testExecuteCommands_WithCommands() {
        // Given
        Runnable runnable = createMockRunnable();
        TimeCommand command1 = new TimeCommand(runnable, DUE_DATE_TIME_LATER);
        TimeCommand command2 = new TimeCommand(runnable, DUE_DATE_TIME);

        executor.addCommand(command1);
        executor.addCommand(command2);

        // When
        executor.executeCommands(DUE_DATE_TIME);

        // Then
        verify(runnable, times(1)).run();
    }

    @Test
    public void testExecuteCommands_InCorrectOrder() {
        // Given
        Runnable runnable = createMockRunnable();
        Runnable runnableLater = createMockRunnable();
        TimeCommand command1 = new TimeCommand(runnable, DUE_DATE_TIME);
        TimeCommand command2 = new TimeCommand(runnableLater, DUE_DATE_TIME);

        executor.addCommand(command1);
        executor.addCommand(command2);

        // When
        executor.executeCommands(DUE_DATE_TIME);

        // Then
        InOrder inOrder = inOrder(runnable, runnableLater);
        inOrder.verify(runnable, times(1)).run();
        inOrder.verify(runnableLater, times(1)).run();
    }

    private PriorityQueue<TimeCommand> getCommandsQueue(TimeCommandExecutor executor) {
        try {
            java.lang.reflect.Field field = TimeCommandExecutor.class.getDeclaredField("commands");
            field.setAccessible(true);
            return (PriorityQueue<TimeCommand>) field.get(executor);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to access 'commands' field", e);
        }
    }

    private Runnable createMockRunnable() {
        return mock(Runnable.class);
    }
}



