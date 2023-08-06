package pl.agh.edu.json.data;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.util.EnumMap;

public record ClientNumberModificationRandomTemporaryEventData(
        String name,
        String calendarDescription,
        String popupDescription,
        int minDurationDays,
        int maxDurationDays,
        EnumMap<HotelVisitPurpose,Double> modifiers,
        double occurrenceProbability,
        String imagePath
) {
}
