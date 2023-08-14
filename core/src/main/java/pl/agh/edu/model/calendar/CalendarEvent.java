package pl.agh.edu.model.calendar;

import java.time.LocalDate;

public record CalendarEvent(LocalDate date,String title,String description) implements Comparable<CalendarEvent> {

    @Override
    public int compareTo(CalendarEvent o) {
        return this.date.compareTo(o.date);
    }
}
