package pl.agh.edu.ui.component.console;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.utils.TimeUtils;

public class LogEntry {
	private final String text;
	private final LogLevel level;
	private final long timeStamp = TimeUtils.millis();

	protected LogEntry(String msg, LogLevel level) {
		this.text = msg;
		this.level = level;
	}

	public LogLevel getLogLevel() {
		return level;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = sdf.format(new Date(timeStamp));
		return formattedDate + "|" + level.getIdentifier() + text;
	}
}
