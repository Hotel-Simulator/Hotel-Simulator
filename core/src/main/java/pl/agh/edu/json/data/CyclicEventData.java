package pl.agh.edu.json.data;

import java.time.LocalDate;

public record CyclicEventData(
    String name,
    String calendarDescription,
    String popupDescription,
    String imagePath,
    LocalDate startDateWithoutYear

) {
}
