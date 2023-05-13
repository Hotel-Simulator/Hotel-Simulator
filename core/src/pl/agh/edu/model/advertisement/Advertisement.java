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


public interface Advertisement {
    double getModifier(LocalTime currentDate);
    LocalDate getExpirationDate();
    String getName();
    double getEffectiveness();
    BigDecimal getCost();
}
