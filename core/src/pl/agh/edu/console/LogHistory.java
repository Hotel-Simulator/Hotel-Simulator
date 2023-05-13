package pl.agh.edu.console;

import com.badlogic.gdx.utils.Array;

public class LogHistory {
    private static LogHistory instance;
    private final Array<LogEntry> logEntries;
    private int numEntries = 20;

    private LogHistory() {
        logEntries = new Array<>();
    }

    public static LogHistory getInstance() {
        if (instance == null) {
            instance = new LogHistory();
        }
        return instance;
    }

    public void setMaxEntries(int numEntries) {
        this.numEntries = numEntries;
    }

    public void addEntry(String msg, LogLevel level) {
        logEntries.add(new LogEntry(msg, level));
        if (logEntries.size > numEntries) {
            logEntries.removeIndex(0);
        }
    }

    public Array<LogEntry> getLogEntries() {
        return logEntries;
    }
}
