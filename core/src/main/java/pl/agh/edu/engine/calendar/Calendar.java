package pl.agh.edu.engine.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class Calendar {
	private static Calendar instance;
	private final Map<LocalDate, List<CalendarEvent>> days;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Calendar.class, new Serializer<Calendar>() {
			@Override
			public void write(Kryo kryo, Output output, Calendar object) {
				kryo.writeObject(output, object.days, KryoConfig.mapOfListSerializer(LocalDate.class, CalendarEvent.class));
			}

			@Override
			public Calendar read(Kryo kryo, Input input, Class<? extends Calendar> type) {
				return new Calendar(kryo.readObject(input, Map.class, KryoConfig.mapOfListSerializer(LocalDate.class, CalendarEvent.class)));
			}
		});
	}

	private Calendar() {
		days = new HashMap<>();
	}

	private Calendar(Map<LocalDate, List<CalendarEvent>> days) {
		this.days = days;
	}

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
