package pl.agh.edu.generator.event_generator;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import pl.agh.edu.json.data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.json.data.ClientNumberModificationRandomTemporaryEventData;
import pl.agh.edu.json.data_loader.JSONEventDataLoader;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.RandomUtils;

public class EventGenerator {
	private static EventGenerator instance;
	private final EventLauncher eventLauncher = EventLauncher.getInstance();
	private static final List<ClientNumberModificationCyclicTemporaryEventData> clientNumberModificationCyclicTemporaryEventData = JSONEventDataLoader.clientNumberModificationCyclicTemporaryEventData;
	private static final List<ClientNumberModificationRandomTemporaryEventData> clientNumberModificationRandomTemporaryEventData = JSONEventDataLoader.clientNumberModificationRandomTemporaryEventData;
	private final Map<ClientNumberModificationRandomTemporaryEventData, Integer> lastYearDate = new HashMap<>();
	private final Time time = Time.getInstance();

	private EventGenerator() {
		initializeClientNumberModificationCyclicTemporaryEvents();
	}

	private void initializeClientNumberModificationRandomTemporaryEventsForThisYear() {
		int daysInYear = Year.isLeap(time.getTime().getYear()) ? 366 : 365;
		clientNumberModificationRandomTemporaryEventData.stream().filter(
				eventData -> {
					boolean willOccur = RandomUtils.randomBooleanWithProbability(eventData.occurrenceProbability());
					if (!willOccur)
						lastYearDate.remove(eventData);
					return willOccur;
				}).map(eventData -> {
					LocalDate launchDate = LocalDate.ofYearDay(time.getTime().getYear(), RandomUtils.randomInt((lastYearDate.get(eventData) == null || lastYearDate.get(
							eventData) < daysInYear / 2) ? 1 : daysInYear / 2, (daysInYear) + 1));
					lastYearDate.put(eventData, launchDate.getDayOfYear());
					return new ClientNumberModificationRandomTemporaryEvent(
							eventData.name(),
							eventData.calendarDescription(),
							eventData.popupDescription(),
							RandomUtils.randomInt(eventData.minDurationDays(), eventData.maxDurationDays() + 1),
							launchDate,
							eventData.modifiers(),
							eventData.imagePath());

				}).forEach(eventLauncher::add);
	}

	public static EventGenerator getInstance() {
		if (instance == null)
			instance = new EventGenerator();
		return instance;
	}

	private void initializeClientNumberModificationCyclicTemporaryEvents() {
		Calendar calendar = Calendar.getInstance();
		Stream.iterate(calendar.getGameStartDate().getYear(), year -> year <= calendar.getGameEndDate().getYear(), year -> year + 1)
				.forEach(year -> clientNumberModificationCyclicTemporaryEventData.forEach(
						event -> eventLauncher.launchCyclicEventsForYear(year, event)));
	}

	public void yearlyUpdate() {
		initializeClientNumberModificationRandomTemporaryEventsForThisYear();
	}
}
