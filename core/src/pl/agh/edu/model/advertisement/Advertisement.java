package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/*
web page
flyers
billboard
tv-ad
newspaper-ad
web-ad
 */


public interface Advertisement extends Comparable<Advertisement> {
    double getModifier(LocalDate currentDate);
    LocalDate getEndDate();
    String getName();
    double getEffectiveness();
    BigDecimal getCostOfPurchase();
    BigDecimal getCostOfMaintenance();

    @Override
    default int compareTo(Advertisement o){
        return - getEndDate().compareTo(o.getEndDate());
    };
}
