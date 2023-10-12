package pl.agh.edu.json.data;

import java.time.LocalDate;

public record CyclicEventData(
    String title,
    String eventAppearancePopupDescription,
    String eventStartPopupDescription,
    String calendarDescription,
    String imagePath,
    LocalDate startDateWithoutYear

) {
}
