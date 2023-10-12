package pl.agh.edu.model.event;

import java.time.LocalDate;

public class ClientNumberModificationEvent extends Event {
	public final int durationInDays;
	public final ClientNumberModifier modifier;

	public ClientNumberModificationEvent(String title,
			String eventAppearancePopupDescription,
			String eventStartPopupDescription,
			String calendarDescription,
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
