package pl.agh.edu.engine.advertisement;

import pl.agh.edu.utils.DataManipulator;

import java.time.LocalDate;

public class AdvertisementDataManipulator extends DataManipulator<AdvertisementCampaign> {
    private AdvertisementDataManipulator() {}

    private static class AdvertisementTypeFilter implements Filter<AdvertisementCampaign> {
        @Override
        public boolean filter(AdvertisementCampaign advertisementCampaign) {
            return advertisementCampaign.advertisementData().type() == AdvertisementType.NEWSPAPER_ADVERTISEMENT;
        }
    }
    private static class AdvertisementStartDateAfterFilter implements Filter<AdvertisementCampaign> {
        private final LocalDate date;
        public AdvertisementStartDateAfterFilter(LocalDate date) {
            this.date = date;
        }
        @Override
        public boolean filter(AdvertisementCampaign advertisementCampaign) {
            return advertisementCampaign.startDate().isAfter(date);
        }
    }

    private static class AdvertisementEndDateBeforeFilter implements Filter<AdvertisementCampaign> {
        private final LocalDate date;
        public AdvertisementEndDateBeforeFilter(LocalDate date) {
            this.date = date;
        }
        @Override
        public boolean filter(AdvertisementCampaign advertisementCampaign) {
            return advertisementCampaign.endDate().isBefore(date);
        }
    }
    private static class AdvertisementEndDateSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return advertisementCampaign1.endDate().compareTo(advertisementCampaign2.endDate());
        }
    }

    private static class AdvertisementEndDateReverseSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return -advertisementCampaign1.endDate().compareTo(advertisementCampaign2.endDate());
        }
    }

    private static class AdvertismetnStartDateSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return advertisementCampaign1.startDate().compareTo(advertisementCampaign2.startDate());
        }
    }

    private static class AdvertismetnStartDateReverseSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return -advertisementCampaign1.startDate().compareTo(advertisementCampaign2.startDate());
        }
    }

    private static class AdvertisementTypeSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return advertisementCampaign1.advertisementData().type().compareTo(advertisementCampaign2.advertisementData().type());
        }
    }

    private static class AdvertisementTypeReverseSorter implements Sorter<AdvertisementCampaign> {
        @Override
        public int compare(AdvertisementCampaign advertisementCampaign1, AdvertisementCampaign advertisementCampaign2) {
            return -advertisementCampaign1.advertisementData().type().compareTo(advertisementCampaign2.advertisementData().type());
        }
    }


    public static class AdvertisementDataManipulatorBuilder {
        private final AdvertisementDataManipulator manipulator;

        public AdvertisementDataManipulatorBuilder() {
            manipulator = new AdvertisementDataManipulator();
        }
        public AdvertisementDataManipulatorBuilder withStartDateAfterFilter(LocalDate date) {
            manipulator.addFilter(new AdvertisementStartDateAfterFilter(date));
            return this;
        }

        public AdvertisementDataManipulatorBuilder withEndDateBeforeFilter(LocalDate date) {
            manipulator.addFilter(new AdvertisementEndDateBeforeFilter(date));
            return this;
        }
        public AdvertisementDataManipulatorBuilder sortByEndDate() {
            manipulator.addSorter(new AdvertisementEndDateSorter());
            return this;
        }

        public AdvertisementDataManipulatorBuilder sortReverseByEndDate() {
            manipulator.addSorter(new AdvertisementEndDateReverseSorter());
            return this;
        }

        public AdvertisementDataManipulatorBuilder sortByStartDate() {
            manipulator.addSorter(new AdvertismetnStartDateSorter());
            return this;
        }

        public AdvertisementDataManipulatorBuilder sortReverseByStartDate() {
            manipulator.addSorter(new AdvertismetnStartDateReverseSorter());
            return this;
        }

        public AdvertisementDataManipulatorBuilder sortByAdvertisementType() {
            manipulator.addSorter(new AdvertisementTypeSorter());
            return this;
        }

        public AdvertisementDataManipulatorBuilder sortByReverseAdvertisementType() {
            manipulator.addSorter(new AdvertisementTypeReverseSorter());
            return this;
        }

        public AdvertisementDataManipulator build() {
            return manipulator;
        }
    }
}
