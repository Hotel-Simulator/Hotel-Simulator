package pl.agh.edu.console;

import java.awt.*;

public enum LogLevel {
    /**
     * The default log level. Prints in white to the console and has no special indicator in the log file.<br>
     * <b>Intentional Use:</b> debugging.
     */
    DEFAULT(new Color(1, 1, 1, 1), ""), /**
     * Use to print errors. Prints in red to the console and has the '<i>ERROR</i>' marking in
     * the log file.<br>
     * <b>Intentional Use:</b> printing internal console errors; debugging.
     */
    ERROR(new Color(217f / 255f, 0, 0, 1), "Error: "), /**
     * Prints in green. Use to print success notifications of events.<br>
     * <b>Intentional Use:</b> Print successful execution of console commands (if needed).
     */
    SUCCESS(new Color(0, 217f / 255f, 0, 1), "Success! "), /**
     * Prints in white with {@literal "> "} prepended to the command. Also has
     * that prepended text as the indicator in the log file.<br>
     * <b>Intentional Use:</b> To be used by the console, alone.
     */
    COMMAND(new Color(1, 1, 1, 1), "> ");

    private final Color color;
    private final String identifier;

    LogLevel (Color c, String identity) {
        this.color = c;
        identifier = identity;
    }

    Color getColor () {
        return color;
    }

    String getIdentifier () {
        return identifier;
    }
}
