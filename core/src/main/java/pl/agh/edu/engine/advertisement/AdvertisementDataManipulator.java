package pl.agh.edu.engine.advertisement;

import pl.agh.edu.utils.DataManipulator;

import java.time.LocalDate;

public class AdvertisementDataManipulator extends DataManipulator<AdvertisementCampaign> {
    public AdvertisementDataManipulator() {
        addFilter(new AdvertisementTypeFilter());
        addFilter(new AdvertisementDateBefore());
        addSorter(new AdvertisementStartDateSorter());
    }



    private static class AdvertisementTypeFilter implements Filter<AdvertisementCampaign> {
        @Override
        public boolean filter(AdvertisementCampaign advertisementCampaign) {
            return advertisementCampaign.advertisementData().type() == AdvertisementType.NEWSPAPER_ADVERTISEMENT;
        }
    }
    private class AdvertisementDateBefore implements Filter<AdvertisementCampaign> {

        private final LocalDate date;
        public AdvertisementDateBefore(LocalDate date) {
            this.date = date;
        }
        @Override
        public boolean filter(AdvertisementCampaign advertisementCampaign) {
            return advertisementCampaign.startDate().isBefore(date);
        }
    }
}
