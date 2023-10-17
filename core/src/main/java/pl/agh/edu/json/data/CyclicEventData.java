package pl.agh.edu.json.data;

import java.time.LocalDate;

public record CyclicEventData(
    String titleProperty,
    String eventAppearancePopupDescriptionProperty,
    String eventStartPopupDescriptionProperty,
    String calendarDescriptionProperty,
    String imagePath,
    LocalDate startDateWithoutYear

) {
}
