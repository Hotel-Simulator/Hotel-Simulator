package pl.agh.edu.model.advertisement.json_data;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;

public record SingleAdvertisementData(
        BigDecimal costOfPurchase,
        int preparationDays,
        EnumMap<HotelVisitPurpose, Double> effectiveness) {
}
