package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ConstantAdvertisement implements Advertisement {
    private final ConstantAdvertisementType type;
    private final double effectiveness;
    private final BigDecimal costOfPurchase;
    private final BigDecimal costOfMaintenance;
    private final LocalDate startDate;

    private  LocalDate endDate;


    public ConstantAdvertisement(ConstantAdvertisementType type, double effectiveness, BigDecimal costOfPurchase, BigDecimal costOfMaintenance, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.effectiveness = effectiveness;
        this.costOfPurchase = costOfPurchase;
        this.costOfMaintenance = costOfMaintenance;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public double getModifier(LocalDate currentDate) {
        if(currentDate.isBefore(endDate) && currentDate.plusDays(1).isAfter(startDate)) return effectiveness;
        else return 0.;
    }


    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
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
        return costOfMaintenance;
    }

    @Override
    public String toString() {
        return "ConstantAdvertisement{" +
                "type=" + type +
                ", effectiveness=" + effectiveness +
                ", costOfPurchase=" + costOfPurchase +
                ", costOfMaintenance=" + costOfMaintenance +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
