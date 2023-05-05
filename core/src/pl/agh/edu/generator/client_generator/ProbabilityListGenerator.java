package pl.agh.edu.generator.client_generator;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;

import java.util.*;

public class ProbabilityListGenerator {

    public static <T> List<T> getProbabilityList(Map<T,Integer> map){
        List<T> list = new ArrayList<>();
        for(Map.Entry<T,Integer> entry : map.entrySet()){
            for(int i=0;i<entry.getValue();i++) list.add(entry.getKey());
        }
        return list;
    }

    public static <T extends Enum<T>,K> EnumMap<T, List<K>> getMapOfProbabilityLists(EnumMap<T, Map<K,Integer>> enumMap, Class<T> tClass){
        EnumMap<T, List<K>> resultEnumMap = new EnumMap<>(tClass);
        for(Map.Entry<T, Map<K, Integer>> entry : enumMap.entrySet()){
            resultEnumMap.put(entry.getKey(),getProbabilityList(entry.getValue()));
        }
        return resultEnumMap;
    }

}
