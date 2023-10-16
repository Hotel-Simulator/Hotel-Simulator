package pl.agh.edu.model.calendar;

import java.time.LocalDate;

import pl.agh.edu.utils.LanguageString;

public record CalendarEvent(LocalDate date, LanguageString title, LanguageString description) implements Comparable<CalendarEvent> {

    @Override
    public int compareTo(CalendarEvent o) {
        return this.date.compareTo(o.date);
    }
}
