package pl.agh.edu.model.event;

import java.time.LocalDate;

public class Event {
	public final String name;
	public final String calendarDescription;
	public final String popupDescription;
	public final LocalDate appearanceDate;
	public final LocalDate startDate;
	public final String imagePath;

	public Event(String name,
			String calendarDescription,
			String popupDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate) {
		this.name = name;
		this.calendarDescription = calendarDescription;
		this.popupDescription = popupDescription;
		this.appearanceDate = appearanceDate;
		this.startDate = startDate;
		this.imagePath = imagePath;
	}
}
