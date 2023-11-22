package pl.agh.edu.engine.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.Year;
import java.util.function.Consumer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.building_cost.BuildingCostMultiplierHandler;
import pl.agh.edu.engine.calendar.Calendar;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.event.permanent.BuildingCostModificationPermanentEvent;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.engine.event.temporary.TemporaryEvent;
import pl.agh.edu.engine.generator.EventGenerator;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.ui.component.modal.ModalManager;

public class EventHandler {

	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	public final Calendar calendar;
	private final BuildingCostMultiplierHandler buildingCostHandler;
	private final EventGenerator eventGenerator;
	private final ClientNumberModificationEventHandler clientNumberModificationEventHandler;
	private Consumer<EventModalData> eventHandlerFunction;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EventHandler.class, new Serializer<EventHandler>() {
			@Override
			public void write(Kryo kryo, Output output, EventHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.calendar);
				kryo.writeObject(output, object.buildingCostHandler);
				kryo.writeObject(output, object.eventGenerator);
				kryo.writeObject(output, object.clientNumberModificationEventHandler);
			}

			@Override
			public EventHandler read(Kryo kryo, Input input, Class<? extends EventHandler> type) {
				return new EventHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, Calendar.class),
						kryo.readObject(input, BuildingCostMultiplierHandler.class),
						kryo.readObject(input, EventGenerator.class),
						kryo.readObject(input, ClientNumberModificationEventHandler.class));
			}
		});
	}

	public EventHandler(BuildingCostMultiplierHandler buildingCostMultiplierHandler,
			HotelScenariosManager hotelScenariosManager,
			ClientNumberModificationEventHandler clientNumberModificationEventHandler) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.calendar = new Calendar();
		this.buildingCostHandler = buildingCostMultiplierHandler;
		this.eventGenerator = new EventGenerator(hotelScenariosManager);
		this.clientNumberModificationEventHandler = clientNumberModificationEventHandler;
	}

	private EventHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			Calendar calendar,
			BuildingCostMultiplierHandler buildingCostHandler,
			EventGenerator eventGenerator,
			ClientNumberModificationEventHandler clientNumberModificationEventHandler) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.calendar = calendar;
		this.buildingCostHandler = buildingCostHandler;
		this.eventGenerator = eventGenerator;
		this.clientNumberModificationEventHandler = clientNumberModificationEventHandler;
	}

	public void yearlyUpdate() {
		if (time.getTime().getYear() == JSONGameDataLoader.startDate.getYear()) {
			addEventCommandsForYear(Year.of(time.getTime().getYear()));
		}
		addEventCommandsForYear(Year.of(time.getTime().getYear() + 1));
	}

	private void addEventCommandsForYear(Year year) {
		eventGenerator.generateClientNumberModificationRandomTemporaryEventsForYear(year).forEach(
				event -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(event));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierStart(event));
					timeCommandExecutor.addCommand(createTimeCommandForModifierEnd(event));
				});

		eventGenerator.generateCyclicTemporaryEventsForYear(year).forEach(
				temporaryEvent -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(temporaryEvent));
					timeCommandExecutor.addCommand(createTimeCommandForCalendarEvent(temporaryEvent));
					timeCommandExecutor.addCommand(createTimeCommandForEventStartPopup(temporaryEvent));
				});

		eventGenerator.generateRandomBuildingCostModificationPermanentEventForYear(year).forEach(
				permanentEvent -> {
					timeCommandExecutor.addCommand(createTimeCommandForEventAppearancePopup(permanentEvent));
					timeCommandExecutor.addCommand(createTimeCommandForBuildingCostModifierStart(permanentEvent));
				});
	}

	private TimeCommand createTimeCommandForEventAppearancePopup(TemporaryEvent temporaryEvent) {
		return new TimeCommand(
				() -> ModalManager.getInstance().showEventModal(new EventModalData(temporaryEvent.title, temporaryEvent.eventAppearancePopupDescription,
						temporaryEvent.imagePath)),
				temporaryEvent.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForEventAppearancePopup(BuildingCostModificationPermanentEvent permanentEvent) {
		return new TimeCommand(
				() -> ModalManager.getInstance().showEventModal(new EventModalData(permanentEvent.title, permanentEvent.eventAppearancePopupDescription,
						permanentEvent.imagePath)),
				permanentEvent.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForBuildingCostModifierStart(BuildingCostModificationPermanentEvent permanentEvent) {
		return new TimeCommand(
				() -> buildingCostHandler.modify(new BigDecimal(permanentEvent.modifierValueInPercent)
						.divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN)),
				permanentEvent.appearanceDate.atTime(LocalTime.NOON));
	}

	private TimeCommand createTimeCommandForCalendarEvent(TemporaryEvent temporaryEvent) {
		CalendarEvent calendarEvent = new CalendarEvent(
				temporaryEvent.startDate,
				temporaryEvent.title,
				temporaryEvent.calendarDescription);
		return new TimeCommand(
				() -> calendar.addEvent(calendarEvent),
				time.getTime());
	}

	private TimeCommand createTimeCommandForEventStartPopup(TemporaryEvent temporaryEvent) {
		return new TimeCommand(
				() -> ModalManager.getInstance().showEventModal(new EventModalData(temporaryEvent.title, temporaryEvent.eventStartPopupDescription,
						temporaryEvent.imagePath)),
				temporaryEvent.startDate.atTime(LocalTime.MIDNIGHT));
	}

	private TimeCommand createTimeCommandForModifierStart(ClientNumberModificationTemporaryEvent event) {
		return new TimeCommand(
				() -> clientNumberModificationEventHandler.add(event.modifier),
				event.appearanceDate.atTime(LocalTime.MIDNIGHT).minusMinutes(1));
	}

	private TimeCommand createTimeCommandForModifierEnd(ClientNumberModificationTemporaryEvent event) {
		return new TimeCommand(
				() -> clientNumberModificationEventHandler.remove(event.modifier),
				event.appearanceDate.atTime(LocalTime.MIDNIGHT).minusMinutes(1));
	}
}
