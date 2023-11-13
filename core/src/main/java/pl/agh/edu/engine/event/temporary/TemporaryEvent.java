package pl.agh.edu.engine.event.temporary;

import java.time.LocalDate;

import pl.agh.edu.utils.LanguageString;

public class TemporaryEvent {
	public final LanguageString title;
	public final LanguageString eventAppearancePopupDescription;
	public final LanguageString eventStartPopupDescription;
	public final LanguageString calendarDescription;
	public final String imagePath;
	public final LocalDate appearanceDate;
	public final LocalDate startDate;

	public TemporaryEvent(LanguageString title,
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
