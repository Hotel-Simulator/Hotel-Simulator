package pl.agh.edu.console;

import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;

public class LogEntry {
    private final String text;
    private final LogLevel level;
    private final long timeStamp;

    protected LogEntry (String msg, LogLevel level) {
        this.text = msg;
        this.level = level;
        timeStamp = TimeUtils.millis();
    }

    public Color getColor () {
        return level.getColor();
    }

    protected String toConsoleString () {
        String r = "";
        if (level.equals(LogLevel.COMMAND)) {
            r += level.getIdentifier();
        }
        r += text;
        return r;
    }

    @Override public String toString () {
        return timeStamp + ": " + level.getIdentifier() + text;
    }
}
