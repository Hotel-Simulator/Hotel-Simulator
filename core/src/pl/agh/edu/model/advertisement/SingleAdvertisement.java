
package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

public class SingleAdvertisement implements Advertisement {
    private final SingleAdvertisementType type;
    private final double effectiveness;
    private final BigDecimal costOfPurchase;
    private  LocalDate emissionDate;


    public SingleAdvertisement(SingleAdvertisementType type, double effectiveness, BigDecimal costOfPurchase, LocalDate emissionDate) {
        this.type = type;
        this.effectiveness = effectiveness;
        this.costOfPurchase = costOfPurchase;
        this.emissionDate = emissionDate;
    }
    @Override
    public double getModifier(LocalDate currentDate) {
        if(currentDate.isEqual(emissionDate)) return effectiveness;
        else if(currentDate.isAfter(emissionDate)) return Math.max(effectiveness - effectiveness * (Duration.between(emissionDate,currentDate).toDays() / 7.),0.);
        return 0.;
    }
    @Override
    public LocalDate getEndDate() {
        return emissionDate;
    }

    @Override
    public String getName() {
        return type.name();
    }
    @Override
    public double getEffectiveness() {
        return effectiveness;
    }

    @Override
    public BigDecimal getCostOfPurchase() {
        return costOfPurchase;
    }

    @Override
    public BigDecimal getCostOfMaintenance() {
        return null;
    }

    @Override
    public String toString() {
        return "SingleAdvertisement{" +
                "type=" + type +
                ", effectiveness=" + effectiveness +
                ", costOfPurchase=" + costOfPurchase +
                ", emissionDate=" + emissionDate +
                '}';
    }
}
