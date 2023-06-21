package pl.agh.edu.model.advertisement;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.model.advertisement.json_data.ConstantAdvertisementData;
import pl.agh.edu.model.advertisement.json_data.SingleAdvertisementData;
import pl.agh.edu.model.Time;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvertisementHandler {
    private static AdvertisementHandler instance;
    private final EnumMap<SingleAdvertisementType, SingleAdvertisementData> simpleAdvertisementData;
    private final EnumMap<ConstantAdvertisementType, ConstantAdvertisementData> constantAdvertisementData;
    private final EnumMap<SingleAdvertisementType,List<SingleAdvertisement> > singleAdvertisements;
    private final EnumMap<ConstantAdvertisementType,ConstantAdvertisement> constantAdvertisements;

    private final LinkedList<Advertisement> advertisementHistory;


    private final Time time;


    private AdvertisementHandler() throws IOException, ParseException {
        this.simpleAdvertisementData = JSONExtractor.getSingleAdvertisementDataFromJSON();
        this.constantAdvertisementData = JSONExtractor.getConstantAdvertisementDataFromJSON();
        this.time = Time.getInstance();
        this.singleAdvertisements = new EnumMap<>(SingleAdvertisementType.class);
        for(SingleAdvertisementType type : SingleAdvertisementType.values()){
            this.singleAdvertisements.put(type,new ArrayList<>());
        }
        this.constantAdvertisements = new EnumMap<>(ConstantAdvertisementType.class);
        this.advertisementHistory = new LinkedList<>();
    }

    public static AdvertisementHandler getInstance() throws IOException, ParseException {
        if(instance == null) instance = new AdvertisementHandler();
        return instance;
    }
    public boolean create(SingleAdvertisementType simpleAdvertisementType, List<LocalDate> emissionDates){

        if(canCreate(simpleAdvertisementType, emissionDates)){
            emissionDates.forEach(emissionDate ->
                    singleAdvertisements.get(simpleAdvertisementType).add(new SingleAdvertisement(
                            simpleAdvertisementType,
                            simpleAdvertisementData.get(simpleAdvertisementType),
                            emissionDate
                    )));

            return true;
        }
;       return false;
    }

    private boolean canCreate(SingleAdvertisementType simpleAdvertisementType, List<LocalDate> emissionDates) {
        return emissionDates.stream().distinct()
                .allMatch(emissionDate -> singleAdvertisements.get(simpleAdvertisementType)
                        .stream()
                        .noneMatch(it -> it.getEmissionDate().equals(emissionDate)));
    }


    public boolean create(ConstantAdvertisementType constantAdvertisementType){

        if(canCreate(constantAdvertisementType)){
            LocalDate startDate = time.getTime().toLocalDate().plusDays(constantAdvertisementData.get(constantAdvertisementType).preparationDays());
            startDate = startDate.minusDays(startDate.getDayOfMonth()-1).plusMonths(1);
            constantAdvertisements.put(constantAdvertisementType,new ConstantAdvertisement(
                    constantAdvertisementType,
                    constantAdvertisementData.get(constantAdvertisementType),
                    startDate
            ));
            return true;
        }
        return false;
    }

    private boolean canCreate(ConstantAdvertisementType constantAdvertisementType) {
        return !constantAdvertisements.containsKey(constantAdvertisementType);
    }

    public void deleteConstantAdvertisement(ConstantAdvertisement constantAdvertisement){
        if(constantAdvertisement.getEndDate() == null){
            constantAdvertisement.setEndDate( time.getTime().toLocalDate().isBefore(constantAdvertisement.getStartDate())
                    ? constantAdvertisement.getStartDate().plusMonths(1)
                    : time.getTime().toLocalDate().minusDays(time.getTime().toLocalDate().getDayOfMonth()-1).plusMonths(1));
        }
    }

    public EnumMap<HotelVisitPurpose,Double> getCumulatedModifier(LocalDate date){
        return Stream.concat(
                singleAdvertisements.entrySet()
                        .stream()
                        .map(entry -> entry.getValue()
                                .stream()
                                .map(singleAdvertisement -> singleAdvertisement.getModifier(date))
                                .reduce(
                                        new EnumMap<>(HotelVisitPurpose.class),
                                        (resultMap, enumMap) -> {
                                            for (HotelVisitPurpose key : enumMap.keySet()) {
                                                Double value = enumMap.get(key);
                                                resultMap.merge(key, value, (a,b) -> Math.min(a+b,simpleAdvertisementData.get(entry.getKey()).effectiveness().get(key)) );
                                            }
                                            return resultMap;})),
                constantAdvertisements.values()
                        .stream()
                        .map(constantAdvertisement -> constantAdvertisement.getModifier(date))

        )
                .reduce(
                        Stream.of(HotelVisitPurpose.values())
                                .collect(Collectors.toMap(
                                        e -> e,
                                        e -> 0.,
                                        (a, b) -> b,
                                        () -> new EnumMap<>(HotelVisitPurpose.class))),
                (resultMap, enumMap) -> {
                    for (HotelVisitPurpose key : enumMap.keySet()) {
                        Double value = enumMap.get(key);
                        resultMap.merge(key, value,Double::sum );
                    }
                    return resultMap;});

    }

    public void update(){
        constantAdvertisements.keySet().removeIf(type -> {
            if(constantAdvertisements.get(type).getEndDate() != null && constantAdvertisements.get(type).getEndDate().equals(time.getTime().toLocalDate())){
                advertisementHistory.addFirst(constantAdvertisements.get(type));
                return true;
            }
            return false;
        });
        singleAdvertisements.values().forEach(list ->list.removeIf(it -> {
          if(it.getEndDate().equals(time.getTime().toLocalDate())){
              advertisementHistory.addFirst(it);
              return true;
          }
          return false;
        }));
    }

    public BigDecimal getCostOfMaintenance(){
        return constantAdvertisements.values()
                .stream()
                .map(ConstantAdvertisement::getCostOfMaintenance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Advertisement> getAdvertisements(){
        return Stream
                .concat(
                        singleAdvertisements.keySet()
                                .stream()
                                .flatMap(key -> singleAdvertisements.get(key).stream()),
                        constantAdvertisements.values()
                                .stream())
                .collect(Collectors.toList());
    }

    public List<Advertisement> getFilteredAdvertisements(boolean pastAdvertisements,boolean currentAdvertisements, boolean futureAdvertisements, String name, String type){
        return Stream
                .concat(getAdvertisements().stream(),
                        advertisementHistory.stream())
                .filter(advertisement -> type == null || type.equals(advertisement.getType()))
                .filter(advertisement -> name == null || name.equals(advertisement.getName()))
                .filter( advertisement -> pastAdvertisements || advertisement.getEndDate() == null ||time.getTime().toLocalDate().isBefore(advertisement.getEndDate()))
                .filter(advertisement -> currentAdvertisements || (time.getTime().toLocalDate().isBefore(advertisement.getStartDate()) ||  (advertisement.getEndDate() != null && !advertisement.getEndDate().isAfter(time.getTime().toLocalDate())) ))
                .filter(advertisement -> futureAdvertisements || !advertisement.getStartDate().isAfter(time.getTime().toLocalDate()))
                .sorted(Advertisement::compareTo)
                .collect(Collectors.toList());

    }
    public Map<String, List<String>> getAdvertisementNames(){
        return  Map.of(
                "single", Stream.of(SingleAdvertisementType.values()).map(type -> type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ")).collect(Collectors.toList()),
                "constant", Stream.of(ConstantAdvertisementType.values()).map(type -> type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ")).collect(Collectors.toList())
        );
    }

    public List<String> getAdvertisementTypes(){return List.of("single","constant");}

    public static void main(String[] args) throws IOException, ParseException {
        AdvertisementHandler advertisementHandler = AdvertisementHandler.getInstance();
        for( ConstantAdvertisementType type: ConstantAdvertisementType.values()){
            System.out.println(advertisementHandler.create(type));;
        }

        System.out.println(advertisementHandler.getAdvertisements());

        for(SingleAdvertisementType type: SingleAdvertisementType.values()){
            System.out.println(advertisementHandler.create(type, List.of(advertisementHandler.time.getTime().toLocalDate().plusDays(10),advertisementHandler.time.getTime().toLocalDate().plusDays(11))));
            System.out.println(advertisementHandler.create(type, List.of(advertisementHandler.time.getTime().toLocalDate().plusDays(10))));

        }
        System.out.println(advertisementHandler.getAdvertisements());
        advertisementHandler.update();
        System.out.println(advertisementHandler.singleAdvertisements);

        System.out.println(advertisementHandler.getAdvertisements());


        System.out.println(advertisementHandler.getCostOfMaintenance());
        System.out.println(advertisementHandler.getAdvertisementNames());

        System.out.println(advertisementHandler.getFilteredAdvertisements(true,true,true,null,null));
        System.out.println(advertisementHandler.getFilteredAdvertisements(true,true,true,null,null).size());

    }




}
