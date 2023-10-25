package pl.agh.edu.engine.event;

import java.time.LocalDate;

import pl.agh.edu.utils.LanguageString;

public class ClientNumberModificationEvent extends Event {
	public final int durationInDays;
	public final ClientNumberModifier modifier;

	public ClientNumberModificationEvent(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LanguageString eventStartPopupDescription,
			LanguageString calendarDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate,
			int durationInDays,
			ClientNumberModifier modifier) {
		super(title, eventAppearancePopupDescription,
				eventStartPopupDescription,
				calendarDescription,
				imagePath,
				appearanceDate,
				startDate);
		this.durationInDays = durationInDays;
		this.modifier = modifier;
	}
}
