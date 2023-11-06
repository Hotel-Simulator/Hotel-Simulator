package pl.agh.edu.engine.event;

import java.time.LocalTime;
import java.time.Year;
import java.util.function.Consumer;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.calendar.Calendar;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.generator.EventGenerator;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;

public class EventHandler {

	private final EventGenerator eventGenerator;
	private final ClientNumberModificationEventHandler clientNumberModificationEventHandler = ClientNumberModificationEventHandler.getInstance();
	private final Calendar calendar = Calendar.getInstance();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final Time time = Time.getInstance();
	private Consumer<EventModalData> eventHandlerFunction;

	public EventHandler(HotelScenariosManager hotelScenariosManager) {
		this.eventGenerator = new EventGenerator(hotelScenariosManager);
	}

	public void yearlyUpdate() {
		if (time.getTime().getYear() == JSONGameDataLoader.startDate.getYear()) {
			addEventCommandsForYear(Year.of(time.getTime().getYear()));
		}
		addEventCommandsForYear(Year.of(time.getTime().getYear() + 1));
	}

	private void addEventCommandsForYear(Year year) {
		eventGenerator.generateClientNumberModificationRandomEventsForYear(year).forEach(
				event -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(event));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierStart(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierEnd(event));
				});

		eventGenerator.generateCyclicEventsForYear(year).forEach(
				event -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(event));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(event));
				});
	}

	private TimeCommand createTimeCommandForEventAppearancePopup(Event event) {
		return new TimeCommand(
				() -> eventHandlerFunction.accept(new EventModalData(event.title, event.eventAppearancePopupDescription, event.imagePath)),
				event.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForCalendarEvent(Event event) {
		CalendarEvent calendarEvent = new CalendarEvent(
				event.startDate,
				event.title,
				event.calendarDescription);
		return new TimeCommand(
				() -> calendar.addEvent(calendarEvent),
				time.getTime());
	}

	private TimeCommand createTimeCommandForEventStartPopup(Event event) {
		return new TimeCommand(
				() -> eventHandlerFunction.accept(new EventModalData(event.title, event.eventStartPopupDescription, event.imagePath)),
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

	public void setEventHandlerFunction(Consumer<EventModalData> eventHandlerFunction) {
		this.eventHandlerFunction = eventHandlerFunction;
	}
}
