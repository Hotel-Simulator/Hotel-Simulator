package pl.agh.edu.model.advertisement.report;

import pl.agh.edu.enums.HotelVisitPurpose;

import java.time.LocalDate;
import java.time.Period;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class AdvertisementReportHandler {
    private static final List<AdvertisementReportData> data = new LinkedList<>();

    public static void collectData(AdvertisementReportData advertisementReportData){
        data.add(advertisementReportData);
    }

    public static List<AdvertisementReportData> getData(){
        return data;
    }

    public static List<AdvertisementReportData> getData(LocalDate startDate){
        if(data.size() == 0) return data;
        if(startDate.isBefore(getMinDate()) || startDate.isAfter(getMaxDate())) throw new IllegalArgumentException("No data available!");
        LocalDate tmp = data.get(0).date();
        int index = Period.between(tmp,startDate).getDays();
        return data.subList(index,data.size());
    }

    public static List<AdvertisementReportData> getData(LocalDate startDate,LocalDate endDate){
        if(startDate.isAfter(endDate)) throw new IllegalArgumentException("Start date can not be after end date!");
        List<AdvertisementReportData> result = getData(startDate);
        if(endDate.isBefore(getMinDate()) || endDate.isAfter(getMaxDate())) throw new IllegalArgumentException("No data available!");

        if(result.size() == 0) return result;
        LocalDate tmp = result.get(0).date();
        int index = Period.between(tmp,endDate).getDays();
        return result.subList(0,index+1);
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
        System.out.println(getData(LocalDate.now(),LocalDate.now()));


    }




}
