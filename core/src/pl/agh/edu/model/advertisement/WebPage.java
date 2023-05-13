package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class WebPage implements Advertisement {
    private final double effectiveness;
    private final BigDecimal costOfCreation;
    private final BigDecimal monthlyMaintenanceCost;

    @Override
    public double getModifier(LocalTime currentDate) {
        return effectiveness;
    }

    @Override
    public LocalDate getExpirationDate() {
        return null;
    }

    @Override
    public String getName() {
        return "Web page";
    }

    @Override
    public double getEffectiveness() {
        return effectiveness;
    }

    @Override
    public BigDecimal getCost() {
        return costPerOneInstance;
    }
}
