package pl.agh.edu.json.data;

import java.time.LocalDate;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public record ClientNumberModificationCyclicTemporaryEventData(
    String name,
    String calendarDescription,
    LocalDate startDateWithoutYear,
    LocalDate endDateWithoutYear,
    EnumMap<HotelVisitPurpose,Double> modifiers
) {
}
