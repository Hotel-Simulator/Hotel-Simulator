package pl.agh.edu.model.event;

import java.time.LocalDate;

public class ClientNumberModificationEvent extends Event {
	public final int durationInDays;
	public final ClientNumberModifier modifier;

	public ClientNumberModificationEvent(String name,
			String calendarDescription,
			String popupDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate,
			int durationInDays,
			ClientNumberModifier modifier) {
		super(name, calendarDescription, popupDescription, imagePath, appearanceDate, startDate);
		this.durationInDays = durationInDays;
		this.modifier = modifier;
	}
}
