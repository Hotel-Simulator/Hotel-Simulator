package pl.agh.edu.model.calendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.json.data_loader.JSONGameDataLoader;

public class Calendar {
	private static Calendar instance;
	private final Map<LocalDate, List<CalendarEvent>> weeks = Stream.iterate(getFirstDayOfWeekDate(JSONGameDataLoader.startDate), date -> date.plusDays(7))
			.limit(ChronoUnit.DAYS.between(JSONGameDataLoader.startDate, JSONGameDataLoader.endDate.plusYears(1)) / 7)
			.collect(Collectors.toMap(
					d -> d,
					d -> new ArrayList<>(),
					(a, b) -> b,
					HashMap::new));

	private Calendar() {}

	public static Calendar getInstance() {
		if (instance == null)
			instance = new Calendar();
		return instance;
	}

	private LocalDate getFirstDayOfWeekDate(LocalDate date) {
		return date.minusDays(date.getDayOfWeek().ordinal());
	}

	public List<CalendarEvent> getEventsForWeek(LocalDate date) {
		return weeks.getOrDefault(getFirstDayOfWeekDate(date), new ArrayList<>());
	}

	public void addEvent(CalendarEvent calendarEvent) {
		weeks.get(getFirstDayOfWeekDate(calendarEvent.date())).add(calendarEvent);
		weeks.get(getFirstDayOfWeekDate(calendarEvent.date())).sort(CalendarEvent::compareTo);
	}

	public LocalDate getGameStartDate() {
		return JSONGameDataLoader.startDate;
	}

	public LocalDate getGameEndDate() {
		return JSONGameDataLoader.endDate;
	}
}
