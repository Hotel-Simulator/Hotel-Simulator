package pl.agh.edu.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListUtils {
    public static <T, U> List<Pair<T, U>> zipLists(List<T> list1, List<U> list2) {
        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("Lists must be of the same size");
        }

        return IntStream.range(0, list1.size())
                .mapToObj(i -> new Pair<>(list1.get(i), list2.get(i)))
                .collect(Collectors.toList());
    }
}
