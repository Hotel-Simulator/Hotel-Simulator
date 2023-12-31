package pl.agh.edu.engine.calendar;

import java.time.LocalDate;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public record CalendarEvent(LocalDate date, LanguageString title, LanguageString description) implements Comparable<CalendarEvent> {
    public static void kryoRegister() {
        KryoConfig.kryo.register(CalendarEvent.class);
    }

    @Override
    public int compareTo(CalendarEvent o) {
        return this.date.compareTo(o.date);
    }
}
