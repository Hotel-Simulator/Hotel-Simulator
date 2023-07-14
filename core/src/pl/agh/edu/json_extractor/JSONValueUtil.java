package pl.agh.edu.json_extractor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.model.bank.json_data.BankData;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JSONValueUtil {

    public static <R> List<R> getArray(JSONArray jsonArray, Function<Object,R> mapper){
        return Stream.of(jsonArray.toArray())
                .map(mapper)
                .collect(Collectors.toList());
    }

    public static LocalDate getLocalDate(String stringDate){
        return LocalDate.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE) ;
    }

    public static int getInt(Long value){
        return value.intValue();
    }

    public static <K,V> Map<K,V> getMap(
            JSONObject jsonObject,
            Function<Object,K> keyMapper,
            Function<Object,V> valueMapper){
        return Stream.of(jsonObject.keySet().toArray()).collect(Collectors.toMap(
                keyMapper,
                valueMapper,
                (a, b) -> b,
                HashMap::new
        ));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(
            JSONObject jsonObject,
            Function<Object, K> keyMapper,
            Function<Object, V> valueMapper,
            Class<K> enumClass) {

        return Stream.of(jsonObject.keySet().toArray())
                .collect(Collectors.toMap(
                        keyMapper,
                        valueMapper,
                        (a, b) -> b,
                        () -> new EnumMap<>(enumClass)
                ));
    }

    public static void main(String[] args) {
        JSONObject jsonObject = JSONDataExtractor.extract(JSONFilePath.CLIENT_CONFIG,"room_size_probabilities",JSONObject.class);
        System.out.println(
                getEnumMap(
                        jsonObject,
                o-> HotelVisitPurpose.valueOf(o.toString()),
                e-> {
                    JSONArray roomSizeProbabilitiesJSONArray = (JSONArray) jsonObject.get(e.toString());
                    return IntStream.range(0, roomSizeProbabilitiesJSONArray.size())
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i + 1,
                                    i -> ((Long) roomSizeProbabilitiesJSONArray.get(i)).intValue(),
                                    (a, b) -> b,
                                    HashMap::new
                            ));
                }, HotelVisitPurpose.class

                )



    );
    }

}
