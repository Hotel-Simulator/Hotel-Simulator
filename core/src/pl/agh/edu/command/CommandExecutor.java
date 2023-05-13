package pl.agh.edu.command;
import com.badlogic.gdx.utils.Queue;
import pl.agh.edu.commands.StartTimeCommand;
import pl.agh.edu.commands.UnknowCommand;
import pl.agh.edu.console.LogHistory;
import pl.agh.edu.console.LogLevel;

public class CommandExecutor {
    private static CommandExecutor instance = null;
    private final Queue<Command> commands = new Queue<>();

    private final LogHistory logHistory ;
    private final CommandHistory commandHistory;

    private CommandExecutor() {
        logHistory = LogHistory.getInstance();
        commandHistory = CommandHistory.getInstance();
    }

    public static CommandExecutor getInstance() {
        if (instance == null) {
            instance = new CommandExecutor();
        }
        return instance;
    }

    public void addCommand(String command) {
        logHistory.addEntry(command, LogLevel.DEFAULT);
        commandHistory.store(command);
        commands.addLast(parseCommand(command));
    }

    public void executeCommands() {
        while(commands.notEmpty()) {
            Command command = commands.removeFirst();
            LogHistory.getInstance().addEntry(command.toString(),LogLevel.COMMAND);
            command.execute();

        }
    }

    private Command parseCommand(String commandString) {
        if (commandString.equals("/time start")) {
            return new StartTimeCommand();
        } else {
            return new UnknowCommand();
        }
    }
}
