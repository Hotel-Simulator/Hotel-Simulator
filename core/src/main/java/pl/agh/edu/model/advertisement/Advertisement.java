package pl.agh.edu.model.advertisement;

import java.time.LocalDate;

public interface Advertisement extends Comparable<Advertisement> {
	LocalDate getStartDate();

	LocalDate getEndDate();

	String getName();

	String getType();

	@Override
	default int compareTo(Advertisement o) {
		return getStartDate().compareTo(o.getStartDate());
	}
}
