
package pl.agh.edu.model.advertisement;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.model.advertisement.json_data.SingleAdvertisementData;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleAdvertisement implements Advertisement {
    private final SingleAdvertisementType type;

    private SingleAdvertisementData singleAdvertisementData;
    private  LocalDate emissionDate;


    public SingleAdvertisement(SingleAdvertisementType type, SingleAdvertisementData singleAdvertisementData, LocalDate emissionDate) {
        this.type = type;
        this.singleAdvertisementData = singleAdvertisementData;
        this.emissionDate = emissionDate;
    }
    public EnumMap<HotelVisitPurpose,Double> getModifier(LocalDate currentDate) {
        if(currentDate.isEqual(emissionDate)) return singleAdvertisementData.effectiveness();
        else if(currentDate.isAfter(emissionDate)) return
                singleAdvertisementData.effectiveness()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> Math.max(e.getValue() - e.getValue() * (ChronoUnit.DAYS.between(emissionDate,currentDate) / 7.),0.),
                                (a,b) -> b,
                                () -> new EnumMap<>(HotelVisitPurpose.class))
                        );
        return  Stream.of(HotelVisitPurpose.values())
                .collect(Collectors.toMap(
                        e -> e,
                        e -> 0.,
                        (a, b) -> b,
                        () -> new EnumMap<>(HotelVisitPurpose.class)));
    }

    @Override
    public LocalDate getStartDate() {
        return emissionDate;
    }

    @Override
    public LocalDate getEndDate() {
        return emissionDate.plusDays(1);
    }

    public LocalDate getEmissionDate() {
        return emissionDate;
    }

    @Override
    public String getName() {
        return type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ");
    }

    @Override
    public String getType() {
        return "single";
    }

    public EnumMap<HotelVisitPurpose,Double> getEffectiveness() {
        return singleAdvertisementData.effectiveness();
    }


    public BigDecimal getCostOfPurchase() {
        return singleAdvertisementData.costOfPurchase();
    }



    @Override
    public String toString() {
        return "SingleAdvertisement{" +
                "type=" + type +
                ", effectiveness=" + singleAdvertisementData.effectiveness() +
                ", costOfPurchase=" + singleAdvertisementData.costOfPurchase() +
                ", emissionDate=" + emissionDate +
                '}';
    }
}
