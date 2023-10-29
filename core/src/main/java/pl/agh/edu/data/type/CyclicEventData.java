package pl.agh.edu.data.type;

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
