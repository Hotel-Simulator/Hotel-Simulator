package pl.agh.edu.engine.event.temporary;

import java.time.LocalDate;

import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.utils.LanguageString;

public class ClientNumberModificationTemporaryEvent extends TemporaryEvent {
	public final int durationInDays;
	public final ClientNumberModifier modifier;

	public ClientNumberModificationTemporaryEvent(LanguageString title,
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
