package pl.agh.edu.model.advertisement;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data.ConstantAdvertisementData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstantAdvertisement implements Advertisement {
    private final ConstantAdvertisementType type;
    private final ConstantAdvertisementData constantAdvertisementData;
    private final LocalDate startDate;

    private  LocalDate endDate;


    public ConstantAdvertisement(ConstantAdvertisementType type, ConstantAdvertisementData constantAdvertisementData, LocalDate startDate) {
        this.type = type;
        this. constantAdvertisementData = constantAdvertisementData;
        this.startDate = startDate;
    }

    public EnumMap<HotelVisitPurpose,Double> getModifier(LocalDate currentDate) {
        if(( endDate == null || currentDate.isBefore(endDate)) && !currentDate.isBefore(startDate)) return constantAdvertisementData.effectiveness();
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

    public LocalDate getStartDate(){return startDate;}

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }
    @Override
    public String getName() {
        return type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ");
    }

    @Override
    public String getType() {
        return "constant";
    }

    public EnumMap<HotelVisitPurpose,Double> getEffectiveness() {
        return constantAdvertisementData.effectiveness();
    }

    public BigDecimal getCostOfPurchase() {
        return constantAdvertisementData.costOfPurchase();
    }

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
