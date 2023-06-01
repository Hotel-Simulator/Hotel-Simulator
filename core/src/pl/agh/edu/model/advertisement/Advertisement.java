package pl.agh.edu.model.advertisement;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;

/*
web page
flyers
billboard
tv-ad
newspaper-ad
web-ad
 */


public interface Advertisement extends Comparable<Advertisement> {
    EnumMap<HotelVisitPurpose,Double> getModifier(LocalDate currentDate);
    LocalDate getEndDate();
    String getName();
    EnumMap<HotelVisitPurpose,Double> getEffectiveness();
    BigDecimal getCostOfPurchase();
    BigDecimal getCostOfMaintenance();

    @Override
    default int compareTo(Advertisement o){
        return - getEndDate().compareTo(o.getEndDate());
    };
}
