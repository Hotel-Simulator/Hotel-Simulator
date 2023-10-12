package pl.agh.edu.json.data;

import pl.agh.edu.model.event.ClientNumberModifier;

public record ClientNumberModificationRandomEventData(
        String title,
        String eventAppearancePopupDescription,
        String eventStartPopupDescription,
        String calendarDescription,
        int minDurationDays,
        int maxDurationDays,
        ClientNumberModifier modifier,
        double occurrenceProbability,
        String imagePath
) {
}
