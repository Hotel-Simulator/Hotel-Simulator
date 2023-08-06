package pl.agh.edu.json.data;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.time.LocalDate;
import java.util.EnumMap;

public record ClientNumberModificationCyclicTemporaryEventData(
    String name,
    String calendarDescription,
    LocalDate startDateWithoutYear,
    LocalDate endDateWithoutYear,
    EnumMap<HotelVisitPurpose,Double> modifiers
) {
}
