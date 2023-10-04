package pl.agh.edu.model.advertisement;

import java.time.LocalDate;

import pl.agh.edu.json.data.AdvertisementData;

public record AdvertisementCampaign(AdvertisementData advertisementData, LocalDate startDate,
                                    LocalDate endDate) implements Comparable<AdvertisementCampaign> {

    @Override
    public int compareTo(AdvertisementCampaign o) {
        return this.startDate.compareTo(o.startDate);
    }
}
