package pl.agh.edu.json.data;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public record SingleAdvertisementData(
        BigDecimal costOfPurchase,
        int preparationDays,
        String imagePath,
        EnumMap<HotelVisitPurpose, Double> effectiveness) {
}
