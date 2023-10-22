package pl.agh.edu.json.data;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.model.event.ClientNumberModifier;

public record ClientNumberModificationRandomEventData(
        String titleProperty,
        String eventAppearancePopupDescriptionProperty,
        String eventStartPopupDescriptionProperty,
        String calendarDescriptionProperty,
        int minDurationDays,
        int maxDurationDays,
        ClientNumberModifier modifier,
        EnumMap<HotelType, BigDecimal> occurrenceProbability,
        String imagePath
) {
}
