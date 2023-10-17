package pl.agh.edu.model.event;

import java.time.LocalDate;

import pl.agh.edu.utils.LanguageString;

public class Event {
	public final LanguageString title;
	public final LanguageString eventAppearancePopupDescription;
	public final LanguageString eventStartPopupDescription;
	public final LanguageString calendarDescription;
	public final String imagePath;
	public final LocalDate appearanceDate;
	public final LocalDate startDate;

	public Event(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LanguageString eventStartPopupDescription,
			LanguageString calendarDescription,
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
