package pl.agh.edu.model.advertisement.report;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.time.LocalDate;
import java.util.EnumMap;

public record AdvertisementReportData(LocalDate date, EnumMap<HotelVisitPurpose,Integer> clientsWithoutAdvertisements, EnumMap<HotelVisitPurpose,Integer> additionalClients) {
}
