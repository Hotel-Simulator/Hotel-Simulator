package pl.agh.edu.model.advertisement.report;

import java.time.LocalDate;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public record AdvertisementReportData(LocalDate date, EnumMap<HotelVisitPurpose,Integer> clientsWithoutAdvertisements, EnumMap<HotelVisitPurpose,Integer> additionalClients) {
}
