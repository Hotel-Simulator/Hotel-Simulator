package pl.agh.edu.generator.event_generator;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.agh.edu.json.data.ClientNumberModificationRandomEventData;
import pl.agh.edu.json.data_loader.JSONEventDataLoader;
import pl.agh.edu.model.event.ClientNumberModificationEvent;
import pl.agh.edu.model.event.Event;
import pl.agh.edu.utils.RandomUtils;

public class EventGenerator {
	private static EventGenerator instance;
	private final Map<String, LocalDate> lastOccurrenceRandomEvents = new HashMap<>();
	private final int monthsBetweenEventAppearanceAndStart = 1;

	private EventGenerator() {}

	public static EventGenerator getInstance() {
		if (instance == null)
			instance = new EventGenerator();
		return instance;
	}

	public List<ClientNumberModificationEvent> generateClientNumberModificationRandomEventsForYear(Year year) {
		return JSONEventDataLoader.clientNumberModificationRandomEventData.stream()
				.filter(eventData -> RandomUtils.randomBooleanWithProbability(eventData.occurrenceProbability()))
				.map(eventData -> {
					LocalDate appearanceDate = getAppearanceDate(eventData, year);
					lastOccurrenceRandomEvents.put(eventData.name(), appearanceDate);
					int durationDays = RandomUtils.randomInt(eventData.minDurationDays(), eventData.maxDurationDays() + 1);
					LocalDate startDate = appearanceDate.plusMonths(monthsBetweenEventAppearanceAndStart);
					return new ClientNumberModificationEvent(
							eventData.name(),
							eventData.calendarDescription().replaceFirst("#", startDate.toString()).replaceFirst("#", String.valueOf(durationDays)),
							eventData.popupDescription().replaceFirst("#", String.valueOf(durationDays)),
							eventData.imagePath(),
							appearanceDate,
							startDate,
							durationDays,
							eventData.modifier());
				}).toList();
	}

	private LocalDate getAppearanceDate(ClientNumberModificationRandomEventData event, Year year) {
		int begin = 1;
		int daysInYear = year.isLeap() ? 366 : 365;
		if (lastOccurrenceRandomEvents.containsKey(event.name())
				&& lastOccurrenceRandomEvents.get(event.name()).getYear() == year.getValue() - 1) {
			LocalDate lastYarDate = lastOccurrenceRandomEvents.get(event.name());
			if (lastYarDate.getDayOfYear() > daysInYear / 2) {
				begin = daysInYear / 2;
			}
		}
		return LocalDate.ofYearDay(year.getValue(), RandomUtils.randomInt(begin, daysInYear + 1));
	}

	public List<Event> generateCyclicEventsForYear(Year year) {
		return JSONEventDataLoader.cyclicEventData.stream()
				.map(
						eventData -> {
							LocalDate appearanceDate = LocalDate.of(year.getValue(), eventData.startDateWithoutYear().getMonth().minus(
									monthsBetweenEventAppearanceAndStart), eventData.startDateWithoutYear().getDayOfMonth());
							return new Event(
									eventData.name(),
									eventData.calendarDescription(),
									eventData.popupDescription(),
									eventData.imagePath(),
									appearanceDate,
									appearanceDate.plusMonths(monthsBetweenEventAppearanceAndStart));
						}).toList();
	}
}
