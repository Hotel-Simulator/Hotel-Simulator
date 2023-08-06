package pl.agh.edu.model.console;

import com.badlogic.gdx.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
    private final String text;
    private final LogLevel level;
    private final long timeStamp;

    protected LogEntry (String msg, LogLevel level) {
        this.text = msg;
        this.level = level;
        timeStamp = TimeUtils.millis();
    }
    public LogLevel getLogLevel () {
        return level;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(new Date(timeStamp));
        return formattedDate + "|" + level.getIdentifier() + text;
    }
}
