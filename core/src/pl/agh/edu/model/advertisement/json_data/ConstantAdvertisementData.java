package pl.agh.edu.model.advertisement.json_data;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;

public record ConstantAdvertisementData(
        BigDecimal costOfPurchase,
        BigDecimal costOfMaintenance,
        int preparationDays,
        String imagePath,
        EnumMap<HotelVisitPurpose, Double> effectiveness) {
}
