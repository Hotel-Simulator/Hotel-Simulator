package pl.agh.edu.engine.advertisement;

import java.time.LocalDate;

import pl.agh.edu.data.type.AdvertisementData;

public record AdvertisementCampaign(AdvertisementData advertisementData,
                                    LocalDate startDate,
                                    LocalDate endDate) implements Comparable<AdvertisementCampaign> {

    @Override
    public int compareTo(AdvertisementCampaign o) {
        return this.startDate.compareTo(o.startDate);
    }
}
