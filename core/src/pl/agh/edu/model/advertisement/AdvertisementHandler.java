package pl.agh.edu.model.advertisement;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.time.Time;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvertisementHandler {
    private static AdvertisementHandler instance;
    private EnumMap<SingleAdvertisementType, Map<String,Integer>> simpleAdvertisementData;
    private EnumMap<ConstantAdvertisementType,Map<String,Integer>> constantAdvertisementData;
    private final EnumMap<SingleAdvertisementType,List<SingleAdvertisement> > singleAdvertisements;
    private final EnumMap<ConstantAdvertisementType,ConstantAdvertisement> constantAdvertisements;

    private final List<Advertisement> advertisementHistory;


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
        this.advertisementHistory = new ArrayList<>();
    }

    public static AdvertisementHandler getInstance() throws IOException, ParseException {
        if(instance == null) instance = new AdvertisementHandler();
        return instance;
    }
    public boolean create(SingleAdvertisementType simpleAdvertisementType, LocalDate emissionDate){
        if(singleAdvertisements.get(simpleAdvertisementType).stream().noneMatch(it -> it.getEmissionDate().equals(emissionDate))){
            singleAdvertisements.get(simpleAdvertisementType).add(new SingleAdvertisement(
                    simpleAdvertisementType,
                    simpleAdvertisementData.get(simpleAdvertisementType).get("effectiveness")/100.,
                    BigDecimal.valueOf(simpleAdvertisementData.get(simpleAdvertisementType).get("cost_of_purchase")),
                    emissionDate
            ));
            return true;
        }
;       return false;
    }

    public boolean create(ConstantAdvertisementType constantAdvertisementType, LocalDate startDate, LocalDate endDate){
        if(!constantAdvertisements.containsKey(constantAdvertisementType)){
            constantAdvertisements.put(constantAdvertisementType,new ConstantAdvertisement(
                    constantAdvertisementType,
                    constantAdvertisementData.get(constantAdvertisementType).get("effectiveness")/100.,
                    BigDecimal.valueOf(constantAdvertisementData.get(constantAdvertisementType).get("cost_of_purchase")),
                    BigDecimal.valueOf(constantAdvertisementData.get(constantAdvertisementType).get("cost_of_maintenance")),
                    startDate,
                    endDate
            ));
            return true;
        }
        return false;
    }

    public double getCumulatedModifier(LocalDate date){
        double modifier = 0.;
        for(SingleAdvertisementType type : SingleAdvertisementType.values()){
            modifier += Math.min(
                    singleAdvertisements.get(type)
                            .stream()
                            .mapToDouble(ad -> ad.getModifier(date))
                            .sum(),
                    simpleAdvertisementData.get(type).get("effectiveness")/100.);
        }


        return modifier + constantAdvertisements.values().stream().mapToDouble(ad -> ad.getModifier(date)).sum();
    }

    public void removeOldAdvertisements(){
        constantAdvertisements.keySet().removeIf(type -> {
            if(constantAdvertisements.get(type).getEndDate().equals(time.getTime().toLocalDate())){
                advertisementHistory.add(constantAdvertisements.get(type));
                return true;
            }
            return false;
        });
        singleAdvertisements.values().forEach(list ->list.removeIf(it -> {
          if(it.getEndDate().equals(time.getTime().toLocalDate())){
              advertisementHistory.add(it);
              return true;
          }
          return false;
        }));
    }

    public List<Advertisement> getAdvertisements(){
        return Stream
                .concat(
                singleAdvertisements.keySet()
                        .stream()
                        .flatMap(key -> singleAdvertisements.get(key).stream()),
                constantAdvertisements.values()
                        .stream())
                .sorted(Advertisement::compareTo).collect(Collectors.toList());
    }
    public BigDecimal getCostOfMaintenance(){
        return constantAdvertisements.values()
                .stream()
                .map(ConstantAdvertisement::getCostOfMaintenance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



    public List<Advertisement> getAdvertisementHistory() {
        return advertisementHistory;
    }

    public static void main(String[] args) throws IOException, ParseException {
        AdvertisementHandler advertisementCreator = AdvertisementHandler.getInstance();
        for( ConstantAdvertisementType type: ConstantAdvertisementType.values()){
            System.out.println(advertisementCreator.create(type,LocalDate.now().plusDays(3),LocalDate.now().plusDays(6)));;
        }

        for(SingleAdvertisementType type: SingleAdvertisementType.values()){
            System.out.println(advertisementCreator.create(type, advertisementCreator.time.getTime().toLocalDate().minusDays(1)));
            System.out.println(advertisementCreator.create(type, advertisementCreator.time.getTime().toLocalDate()));

        }
        System.out.println(advertisementCreator.singleAdvertisements);
        advertisementCreator.removeOldAdvertisements();
        System.out.println(advertisementCreator.singleAdvertisements);
        System.out.println(advertisementCreator.advertisementHistory);

        System.out.println(advertisementCreator.getAdvertisements());
        System.out.println(advertisementCreator.getCostOfMaintenance());

    }



}
