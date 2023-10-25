package pl.agh.edu.data.type;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public record AdvertisementData(
        AdvertisementType type,
        BigDecimal costPerDay,
        EnumMap<HotelVisitPurpose, BigDecimal> effectiveness
) {
}
