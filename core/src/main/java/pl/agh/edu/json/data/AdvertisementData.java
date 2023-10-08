package pl.agh.edu.json.data;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.model.advertisement.AdvertisementType;

public record AdvertisementData(
        AdvertisementType type,
        BigDecimal costPerDay,
        EnumMap<HotelVisitPurpose, BigDecimal> effectiveness
) {
}
