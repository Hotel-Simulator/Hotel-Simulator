package pl.agh.edu.json.data_loader;

import org.json.simple.JSONObject;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.model.advertisement.ConstantAdvertisementType;
import pl.agh.edu.model.advertisement.SingleAdvertisementType;
import pl.agh.edu.json.data.ConstantAdvertisementData;
import pl.agh.edu.json.data.SingleAdvertisementData;

import java.util.EnumMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONAdvertisementDataLoader {
    private static final String JSON_FILE_PATH = JSONFilePath.ADVERTISEMENT_CONFIG.get();

    public static double multiplier;
    public static EnumMap<SingleAdvertisementType, SingleAdvertisementData> singleAdvertisementData;
    public static EnumMap<ConstantAdvertisementType, ConstantAdvertisementData> constantAdvertisementData;

    private JSONAdvertisementDataLoader(){}

    static{
        load();
    }

    public static void load(){
        multiplier = JSONDataExtractor.extract(JSON_FILE_PATH,"multiplier",Double.class);
        singleAdvertisementData = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"single_advertisement_data",JSONObject.class),
                entry -> {
                    JSONObject data = (JSONObject) entry.getValue();
                    JSONObject effectivenessData = (JSONObject) data.get("effectiveness");
                    return  new SingleAdvertisementData(
                            JSONValueUtil.getBigDecimal((Long)data.get("cost_of_purchase")),
                            JSONValueUtil.getInt((Long)data.get("preparation_days")),
                            (String)data.get("image_path"),
                            Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                                    h -> h,
                                    h -> ((Long)effectivenessData.get(h.toString())) * multiplier,
                                    (a,b) -> b,
                                    () -> new EnumMap<>(HotelVisitPurpose.class)
                            )));
                },
                SingleAdvertisementType.class
        );

        constantAdvertisementData = JSONValueUtil.getEnumMap(
                JSONDataExtractor.extract(JSON_FILE_PATH,"constant_advertisement_data",JSONObject.class),
                entry -> {
                    JSONObject data = (JSONObject) entry.getValue();
                    JSONObject effectivenessData = (JSONObject) data.get("effectiveness");
                    return  new ConstantAdvertisementData(
                            JSONValueUtil.getBigDecimal((Long)data.get("cost_of_purchase")),
                            JSONValueUtil.getBigDecimal((Long)data.get("cost_of_maintenance")),
                            JSONValueUtil.getInt((Long)data.get("preparation_days")),
                            (String)data.get("image_path"),
                            Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                                    h -> h,
                                    h -> ((Long)effectivenessData.get(h.toString())) * multiplier,
                                    (a,b) -> b,
                                    () -> new EnumMap<>(HotelVisitPurpose.class)
                            )));
                },
                ConstantAdvertisementType.class
        );
    }
}
