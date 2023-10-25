package pl.agh.edu.ui.component.console;

import com.badlogic.gdx.utils.Array;

public class CommandHistory {
	private static CommandHistory instance;

	private final Array<String> commands = new Array<>(true, 20);
	private final LogHistory logHistory = LogHistory.getInstance();
	private int index;

	private CommandHistory() {}

	public static CommandHistory getInstance() {
		if (instance == null) {
			instance = new CommandHistory();
		}
		return instance;
	}

	public void store(String command) {
		if (commands.size > 0 && isLastCommand(command)) {
			return;
		}
		commands.insert(0, command);
		indexAtBeginning();
	}

	public String getPreviousCommand() {
		index++;

		if (commands.size == 0) {
			indexAtBeginning();
			return "";
		} else if (index >= commands.size) {
			index = 0;
		}

		return commands.get(index);
	}

	public String getNextCommand() {
		index--;
		if (commands.size <= 1 || index < 0) {
			indexAtBeginning();
			return "";
		}
		return commands.get(index);
	}

	private boolean isLastCommand(String command) {
		return command.equals(commands.first());
	}

	private void indexAtBeginning() {
		index = -1;
	}
}
