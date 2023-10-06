package pl.agh.edu.management.event;

import java.time.LocalTime;

import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.model.calendar.CalendarEvent;
import pl.agh.edu.model.event.ClientNumberModificationEvent;
import pl.agh.edu.model.event.Event;
import pl.agh.edu.model.event.EventPopup;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class EventHandler {

	EventGenerator eventGenerator = EventGenerator.getInstance();
	ClientNumberModificationEventHandler clientNumberModificationEventHandler;
	Calendar calendar = Calendar.getInstance();
	TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	public void yearlyUpdate() {
		eventGenerator.generateClientNumberModificationRandomEventsForThisYear().forEach(
				event -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(event));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierStart(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierEnd(event));
				});

		eventGenerator.generateCyclicEventsForThisYear().forEach(
				event -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(event));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(event));
				});
	}

	private TimeCommand createTimeCommandForEventAppearancePopup(Event event) {
		return new TimeCommand(
				() -> new EventPopup(event.name, event.popupDescription, event.imagePath),
				event.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForCalendarEvent(Event event) {
		CalendarEvent calendarEvent = new CalendarEvent(
				event.startDate,
				event.name,
				event.calendarDescription);
		return new TimeCommand(
				() -> calendar.addEvent(calendarEvent),
				event.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForEventStartPopup(Event event) {
		return new TimeCommand(
				() -> new EventPopup(event.name, event.popupDescription, event.imagePath),
				event.startDate.atTime(LocalTime.MIDNIGHT));
	}

	private TimeCommand createTimeCommandForModifierStart(ClientNumberModificationEvent event) {
		return new TimeCommand(
				() -> clientNumberModificationEventHandler.add(event.modifier),
				event.appearanceDate.atTime(LocalTime.MIDNIGHT).minusMinutes(1));
	}

	private TimeCommand createTimeCommandForModifierEnd(ClientNumberModificationEvent event) {
		return new TimeCommand(
				() -> clientNumberModificationEventHandler.remove(event.modifier),
				event.appearanceDate.atTime(LocalTime.MIDNIGHT).minusMinutes(1));
	}
}
