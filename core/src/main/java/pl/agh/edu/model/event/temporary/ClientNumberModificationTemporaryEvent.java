package pl.agh.edu.model.event.temporary;

import java.time.LocalDate;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public class ClientNumberModificationTemporaryEvent extends TemporaryEvent {
	private final EnumMap<HotelVisitPurpose, Double> modifier;

	public ClientNumberModificationTemporaryEvent(LocalDate startDate,
			LocalDate endDate,
			EnumMap<HotelVisitPurpose, Double> modifier) {
		super(startDate, endDate);
		this.modifier = modifier;
	}

	public EnumMap<HotelVisitPurpose, Double> getModifier() {
		return modifier;
	}
}
