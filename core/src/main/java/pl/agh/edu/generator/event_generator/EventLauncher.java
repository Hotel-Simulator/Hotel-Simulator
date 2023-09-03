package pl.agh.edu.generator.event_generator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

import pl.agh.edu.json.data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.model.calendar.CalendarEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.utils.RandomUtils;

public class EventLauncher {
	private static EventLauncher instance;
	private final Calendar calendar = Calendar.getInstance();
	private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
	private final PriorityQueue<ClientNumberModificationRandomTemporaryEvent> eventsToLaunch = new PriorityQueue<>(Comparator.comparing(
			ClientNumberModificationRandomTemporaryEvent::launchDate));
	private final Time time = Time.getInstance();

	private EventLauncher() {}

	public static EventLauncher getInstance() {
		if (instance == null)
			instance = new EventLauncher();
		return instance;
	}

	public boolean add(ClientNumberModificationRandomTemporaryEvent event) {
		return eventsToLaunch.add(event);
	}

	public void launchEventsForToday() {
		ClientNumberModificationRandomTemporaryEvent event = eventsToLaunch.peek();
		while (event != null && event.launchDate().equals(time.getTime().toLocalDate())) {
			launch(Objects.requireNonNull(eventsToLaunch.poll()));
			event = eventsToLaunch.peek();
		}
	}

	public void launchCyclicEventsForYear(Integer year, ClientNumberModificationCyclicTemporaryEventData event) {
		calendar.addEvent(
				new CalendarEvent(
						LocalDate.of(year, event.startDateWithoutYear().getMonth(), event.startDateWithoutYear().getDayOfMonth()),
						event.name(),
						event.calendarDescription()));
		clientNumberModificationTemporaryEventHandler.add(
				new ClientNumberModificationTemporaryEvent(
						LocalDate.of(year, event.startDateWithoutYear().getMonth(), event.startDateWithoutYear().getDayOfMonth()),
						LocalDate.of(year, event.endDateWithoutYear().getMonth(), event.endDateWithoutYear().getDayOfMonth()),
						event.modifiers()

				));
	}

	private void launch(ClientNumberModificationRandomTemporaryEvent event) {
		LocalDate startDate = RandomUtils.randomDate(
				event.launchDate().plusDays(80),
				event.launchDate().plusDays(100));
		String calendarDescription = event.calendarDescription().replaceFirst("#", startDate.toString()).replaceFirst("#", String.valueOf(event.durationDays()));
		String popupDescription = event.popupDescription().replaceFirst("#", String.valueOf(event.durationDays()));
		calendar.addEvent(new CalendarEvent(startDate, event.name(), calendarDescription));
		clientNumberModificationTemporaryEventHandler.add(new ClientNumberModificationTemporaryEvent(startDate, startDate.plusDays(event.durationDays()), event.modifiers()));
		new PopUpEvent(event.name(), popupDescription, event.imagePath());
	}

}
