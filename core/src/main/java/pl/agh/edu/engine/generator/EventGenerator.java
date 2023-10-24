package pl.agh.edu.engine.generator;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.agh.edu.data.loader.JSONEventDataLoader;
import pl.agh.edu.data.type.ClientNumberModificationRandomEventData;
import pl.agh.edu.engine.event.ClientNumberModificationEvent;
import pl.agh.edu.engine.event.Event;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class EventGenerator {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
	private final Map<String, LocalDate> lastOccurrenceRandomEvents = new HashMap<>();
	private final int monthsBetweenEventAppearanceAndStart = 1;
	private final HotelScenariosManager hotelScenariosManager;

	public EventGenerator(HotelScenariosManager hotelScenariosManager) {
		this.hotelScenariosManager = hotelScenariosManager;
	}

	private static LanguageString getLanguageStringWithDateAndNoDays(String string, LocalDate date, int noDays) {
		return new LanguageString(string, List.of(
				Pair.of("{{date}}", date.format(formatter)),
				Pair.of("{{noDays}}", String.valueOf(noDays))));
	}

	private static LanguageString getLanguageStringWithNoDays(String string, int noDays) {
		return new LanguageString(string, List.of(
				Pair.of("{{noDays}}", String.valueOf(noDays))));
	}

	public List<ClientNumberModificationEvent> generateClientNumberModificationRandomEventsForYear(Year year) {
		return JSONEventDataLoader.clientNumberModificationRandomEventData.stream()
				.filter(eventData -> RandomUtils.randomBooleanWithProbability(eventData.occurrenceProbability().get(hotelScenariosManager.hotelType).doubleValue()))
				.map(eventData -> {
					LocalDate appearanceDate = getAppearanceDate(eventData, year);
					lastOccurrenceRandomEvents.put(eventData.titleProperty(), appearanceDate);
					int durationDays = RandomUtils.randomInt(eventData.minDurationDays(), eventData.maxDurationDays() + 1);
					LocalDate startDate = appearanceDate.plusMonths(monthsBetweenEventAppearanceAndStart);
					return new ClientNumberModificationEvent(
							new LanguageString(eventData.titleProperty()),
							getLanguageStringWithDateAndNoDays(eventData.eventAppearancePopupDescriptionProperty(), startDate, durationDays),
							getLanguageStringWithDateAndNoDays(eventData.eventStartPopupDescriptionProperty(), startDate, durationDays),
							getLanguageStringWithNoDays(eventData.calendarDescriptionProperty(), durationDays),
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
		if (lastOccurrenceRandomEvents.containsKey(event.titleProperty())
				&& lastOccurrenceRandomEvents.get(event.titleProperty()).getYear() == year.getValue() - 1) {
			LocalDate lastYarDate = lastOccurrenceRandomEvents.get(event.titleProperty());
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
									new LanguageString(eventData.titleProperty()),
									new LanguageString(eventData.eventAppearancePopupDescriptionProperty()),
									new LanguageString(eventData.eventStartPopupDescriptionProperty()),
									new LanguageString(eventData.calendarDescriptionProperty()),
									eventData.imagePath(),
									appearanceDate,
									appearanceDate.plusMonths(monthsBetweenEventAppearanceAndStart));
						}).toList();
	}
}
