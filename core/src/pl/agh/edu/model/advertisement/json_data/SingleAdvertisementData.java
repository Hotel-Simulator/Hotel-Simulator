package pl.agh.edu.model.advertisement.json_data;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.math.BigDecimal;
import java.util.EnumMap;

public record SingleAdvertisementData(
        BigDecimal costOfPurchase,
        EnumMap<HotelVisitPurpose, Double> effectiveness) {
}
