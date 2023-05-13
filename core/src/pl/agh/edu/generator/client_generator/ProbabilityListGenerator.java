package pl.agh.edu.generator.client_generator;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProbabilityListGenerator {

    public static <T> List<T> getProbabilityList(Map<T,Integer> map){

        return map.entrySet().stream()
                .flatMap(entry -> IntStream
                        .range(0, entry.getValue())
                        .mapToObj(i -> entry.getKey()))
                .collect(Collectors.toList());
    }

    public static <T extends Enum<T>,K> EnumMap<T, List<K>> getMapOfProbabilityLists(EnumMap<T, Map<K,Integer>> enumMap, Class<T> tClass){
        EnumMap<T, List<K>> resultEnumMap = new EnumMap<>(tClass);
        for(Map.Entry<T, Map<K, Integer>> entry : enumMap.entrySet()){
            resultEnumMap.put(entry.getKey(),getProbabilityList(entry.getValue()));
        }
        return resultEnumMap;
    }

}
