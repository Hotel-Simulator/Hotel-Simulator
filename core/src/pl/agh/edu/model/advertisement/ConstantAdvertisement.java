package pl.agh.edu.model.advertisement;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.model.advertisement.json_data.ConstantAdvertisementData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstantAdvertisement implements Advertisement {
    private final ConstantAdvertisementType type;
    private final ConstantAdvertisementData constantAdvertisementData;
    private final LocalDate startDate;

    private  LocalDate endDate;


    public ConstantAdvertisement(ConstantAdvertisementType type, ConstantAdvertisementData constantAdvertisementData, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this. constantAdvertisementData = constantAdvertisementData;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public EnumMap<HotelVisitPurpose,Double> getModifier(LocalDate currentDate) {
        if(currentDate.isBefore(endDate) && currentDate.plusDays(1).isAfter(startDate)) return constantAdvertisementData.effectiveness();
        else  return  Stream.of(HotelVisitPurpose.values())
                .collect(Collectors.toMap(
                        e -> e,
                        e -> 0.,
                        (a, b) -> b,
                        () -> new EnumMap<>(HotelVisitPurpose.class)));
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
    public EnumMap<HotelVisitPurpose,Double> getEffectiveness() {
        return constantAdvertisementData.effectiveness();
    }

    @Override
    public BigDecimal getCostOfPurchase() {
        return constantAdvertisementData.costOfPurchase();
    }

    @Override
    public BigDecimal getCostOfMaintenance() {
        return constantAdvertisementData.costOfMaintenance();
    }

    @Override
    public String toString() {
        return "ConstantAdvertisement{" +
                "type=" + type +
                ", effectiveness=" + constantAdvertisementData.effectiveness() +
                ", costOfPurchase=" + constantAdvertisementData.costOfPurchase() +
                ", costOfMaintenance=" + constantAdvertisementData.costOfMaintenance() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
