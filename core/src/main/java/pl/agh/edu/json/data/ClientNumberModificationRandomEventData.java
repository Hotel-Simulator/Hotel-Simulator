package pl.agh.edu.json.data;

import pl.agh.edu.model.event.ClientNumberModifier;

public record ClientNumberModificationRandomEventData(
        String name,
        String calendarDescription,
        String popupDescription,
        int minDurationDays,
        int maxDurationDays,
        ClientNumberModifier modifier,
        double occurrenceProbability,
        String imagePath
) {
}
