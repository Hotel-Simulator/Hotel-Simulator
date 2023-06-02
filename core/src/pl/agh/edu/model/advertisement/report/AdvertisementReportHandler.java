package pl.agh.edu.model.advertisement.report;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class AdvertisementReportHandler {
    private static final List<AdvertisementReportData> data = new ArrayList<>();

    public static void collectData(AdvertisementReportData advertisementReportData){
        data.add(advertisementReportData);
    }

    public static List<AdvertisementReportData> getData(){
        return data.stream().toList();
    }

    public static List<AdvertisementReportData> getData(LocalDate startDate){
        return data.stream()
                .dropWhile(advertisementReportData -> advertisementReportData.date().isBefore(startDate))
                .collect(Collectors.toList());
    }

    public static List<AdvertisementReportData> getData(LocalDate startDate,LocalDate endDate){
        return data.stream()
                .dropWhile(advertisementReportData -> advertisementReportData.date().isBefore(startDate))
                .takeWhile(advertisementReportData -> advertisementReportData.date().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public static LocalDate getMinDate(){
        if(data.size() == 0) throw new IllegalStateException("No data available!");
        return data.get(0).date();
    }

    public static LocalDate getMaxDate(){
        if(data.size() == 0) throw new IllegalStateException("No data available!");
        return data.get(data.size()-1).date();
    }

    public static void main(String[] args) {
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(7),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(6),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(5),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(4),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(3),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(2),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now().minusDays(1),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));
        AdvertisementReportHandler.collectData(new AdvertisementReportData(LocalDate.now(),new EnumMap<>(HotelVisitPurpose.class),new EnumMap<>(HotelVisitPurpose.class)));

        System.out.println(AdvertisementReportHandler.getData().size());
        System.out.println(AdvertisementReportHandler.getData(LocalDate.now().minusDays(1)).size());
        System.out.println(getData(LocalDate.now().minusDays(5),LocalDate.now().minusDays(2)));


    }




}
