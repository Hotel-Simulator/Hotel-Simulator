package pl.agh.edu.generator.event_generator;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.time.LocalDate;
import java.util.EnumMap;

public record ClientNumberModificationRandomTemporaryEvent(
        String name,
        String calendarDescription,
        String popupDescription,
        int durationDays,
        LocalDate launchDate,
        EnumMap<HotelVisitPurpose,Double> modifiers,
        String imagePath
) {
}
