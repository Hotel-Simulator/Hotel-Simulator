package pl.agh.edu.data.type;


import java.math.BigDecimal;

public record RandomBuildingCostModificationPermanentEventData(
        String titleProperty,
        String positiveEventAppearancePopupDescriptionProperty,
        String negativeEventAppearancePopupDescriptionProperty,
        BigDecimal occurrenceProbability,
        int minModifierValueInPercent,
        int maxModifierValueInPercent,
        String imagePath
) {
}
