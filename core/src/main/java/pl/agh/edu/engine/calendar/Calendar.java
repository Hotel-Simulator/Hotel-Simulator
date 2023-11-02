package pl.agh.edu.engine.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendar {
	private static Calendar instance;
	private final Map<LocalDate, List<CalendarEvent>> days = new HashMap<>();

	private Calendar() {}

	public static Calendar getInstance() {
		if (instance == null)
			instance = new Calendar();
		return instance;
	}

	public List<CalendarEvent> getEventsForDate(LocalDate date) {
		return days.getOrDefault(date, new ArrayList<>());
	}

	public void addEvent(CalendarEvent calendarEvent) {
		if (!days.containsKey(calendarEvent.date())) {
			days.put(calendarEvent.date(), new ArrayList<>());
		}
		days.get(calendarEvent.date()).add(calendarEvent);
	}
}
