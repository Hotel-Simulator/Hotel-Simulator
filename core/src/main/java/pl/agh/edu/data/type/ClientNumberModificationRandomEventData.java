package pl.agh.edu.data.type;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.engine.hotel.HotelType;

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
