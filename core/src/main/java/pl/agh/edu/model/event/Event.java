package pl.agh.edu.model.event;

import java.time.LocalDate;

public class Event {
	public final String title;
	public final String eventAppearancePopupDescription;
	public final String eventStartPopupDescription;
	public final String calendarDescription;
	public final String imagePath;
	public final LocalDate appearanceDate;
	public final LocalDate startDate;

	public Event(String title,
			String eventAppearancePopupDescription,
			String eventStartPopupDescription,
			String calendarDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate) {
		this.title = title;
		this.eventAppearancePopupDescription = eventAppearancePopupDescription;
		this.eventStartPopupDescription = eventStartPopupDescription;
		this.calendarDescription = calendarDescription;
		this.imagePath = imagePath;
		this.appearanceDate = appearanceDate;
		this.startDate = startDate;
	}
}
