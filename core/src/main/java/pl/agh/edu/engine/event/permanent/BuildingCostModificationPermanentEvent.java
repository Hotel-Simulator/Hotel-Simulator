package pl.agh.edu.engine.event.permanent;

import java.time.LocalDate;

import pl.agh.edu.utils.LanguageString;

public class BuildingCostModificationPermanentEvent {
	public final LanguageString title;
	public final LanguageString eventAppearancePopupDescription;
	public final LocalDate appearanceDate;
	public final int modifierValueInPercent;
	public final String imagePath;

	public BuildingCostModificationPermanentEvent(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LocalDate appearanceDate,
			int modifierValueInPercent,
			String imagePath) {
		this.title = title;
		this.eventAppearancePopupDescription = eventAppearancePopupDescription;
		this.appearanceDate = appearanceDate;
		this.modifierValueInPercent = modifierValueInPercent;
		this.imagePath = imagePath;
	}
}
