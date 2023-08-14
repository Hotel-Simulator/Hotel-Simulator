package pl.agh.edu.model.calendar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.json.data_loader.JSONGameDataLoader;

public class Calendar {
	private static Calendar instance;
	private final Map<LocalDate, List<CalendarEvent>> weeks;

	private final LocalDate gameStartDate;
	private final LocalDate gameEndDate;

	private Calendar() {
		this.gameStartDate = JSONGameDataLoader.startDate;
		this.gameEndDate = JSONGameDataLoader.endDate;

		LocalDate mondayDate = getFirstDayOfWeekDate(gameStartDate);
		weeks = Stream.iterate(mondayDate, date -> date.plusDays(7))
				.limit(ChronoUnit.DAYS.between(gameStartDate, gameEndDate.plusYears(1)) / 7)
				.collect(Collectors.toMap(
						d -> d,
						d -> new ArrayList<>(),
						(a, b) -> b,
						HashMap::new));
	}

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
		return gameStartDate;
	}

	public LocalDate getGameEndDate() {
		return gameEndDate;
	}
}
