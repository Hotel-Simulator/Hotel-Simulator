package pl.agh.edu.ui.component.console;

import com.badlogic.gdx.utils.Array;

public class LogHistory {
	private static LogHistory instance;
	private final Array<LogEntry> logEntries = new Array<>();

	private LogHistory() {}

	public static LogHistory getInstance() {
		if (instance == null) {
			instance = new LogHistory();
		}
		return instance;
	}

	public void addEntry(String msg, LogLevel level) {
		logEntries.add(new LogEntry(msg, level));
	}

	public Array<LogEntry> getLogEntries() {
		return logEntries;
	}
}
