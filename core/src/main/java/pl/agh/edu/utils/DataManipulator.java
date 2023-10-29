package pl.agh.edu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class DataManipulator<T> {
	private final List<Filter<T>> filterList = new ArrayList<>();
	private final List<Sorter<T>> sorterList = new ArrayList<>();

	protected void addFilter(Filter<T> filter) {
		filterList.add(filter);
	}

	protected void addSorter(Sorter<T> sorter) {
		sorterList.add(sorter);
	}

	private boolean filterStream(T t) {
		return filterList.stream().allMatch(filter -> filter.filter(t));
	}

	private int compareStream(T t1, T t2) {
		return sorterList.stream()
				.mapToInt(comparator -> comparator.compare(t1, t2))
				.filter(result -> result != 0)
				.findFirst()
				.orElse(0);
	}

	public Stream<T> filterAndSort(Stream<T> stream) {
		return stream
				.filter(this::filterStream)
				.sorted(this::compareStream);
	}

	protected interface Filter<T> {
		boolean filter(T t);
	}

	protected interface Sorter<T> {
		int compare(T t1, T t2);
	}
}
