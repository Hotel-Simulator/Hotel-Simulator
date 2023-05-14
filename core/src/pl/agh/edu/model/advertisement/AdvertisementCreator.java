package pl.agh.edu.model.advertisement;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.time.Time;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

public class AdvertisementCreator {
    private static AdvertisementCreator instance;
    private EnumMap<SingleAdvertisementType, Map<String,Integer>> simpleAdvertisementData;
    private EnumMap<ConstantAdvertisementType,Map<String,Integer>> constantAdvertisementData;

    private final Time time;


    private AdvertisementCreator() throws IOException, ParseException {
        this.simpleAdvertisementData = JSONExtractor.getSingleAdvertisementDataFromJSON();
        this.constantAdvertisementData = JSONExtractor.getConstantAdvertisementDataFromJSON();
        this.time = Time.getInstance();
    }

    public static AdvertisementCreator getInstance() throws IOException, ParseException {
        if(instance == null) instance = new AdvertisementCreator();
        return instance;
    }
    public Advertisement create(SingleAdvertisementType simpleAdvertisementType, LocalDate emissionDate){
        return new SingleAdvertisement(
                simpleAdvertisementType,
                simpleAdvertisementData.get(simpleAdvertisementType).get("effectiveness")/100.,
                BigDecimal.valueOf(simpleAdvertisementData.get(simpleAdvertisementType).get("cost_of_purchase")),
                emissionDate
        );
    }

    public Advertisement create(ConstantAdvertisementType constantAdvertisementType, LocalDate startDate, LocalDate endDate){
        return new ConstantAdvertisement(
                constantAdvertisementType,
                constantAdvertisementData.get(constantAdvertisementType).get("effectiveness")/100.,
                BigDecimal.valueOf(constantAdvertisementData.get(constantAdvertisementType).get("cost_of_purchase")),
                BigDecimal.valueOf(constantAdvertisementData.get(constantAdvertisementType).get("cost_of_maintenance")),
                startDate,
                endDate
                );
    }


    public static void main(String[] args) throws IOException, ParseException {
        AdvertisementCreator advertisementCreator = AdvertisementCreator.getInstance();
        for( ConstantAdvertisementType type: ConstantAdvertisementType.values()){
            System.out.println(advertisementCreator.create(type,LocalDate.now().plusDays(3),LocalDate.now().plusDays(6)));
        }
        for(SingleAdvertisementType type: SingleAdvertisementType.values()){
            System.out.println(advertisementCreator.create(type, LocalDate.now().plusDays(4)));
        }
    }


}
