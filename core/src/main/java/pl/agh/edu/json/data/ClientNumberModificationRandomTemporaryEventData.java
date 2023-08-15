package pl.agh.edu.json.data;

import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

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
