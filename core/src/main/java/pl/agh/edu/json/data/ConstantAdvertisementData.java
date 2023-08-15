package pl.agh.edu.json.data;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public record ConstantAdvertisementData(
        BigDecimal costOfPurchase,
        BigDecimal costOfMaintenance,
        int preparationDays,
        String imagePath,
        EnumMap<HotelVisitPurpose, Double> effectiveness) {
}
