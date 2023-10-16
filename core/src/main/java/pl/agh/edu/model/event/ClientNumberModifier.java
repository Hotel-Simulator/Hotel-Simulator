package pl.agh.edu.model.event;

import java.math.BigDecimal;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;

public class ClientNumberModifier {
	private final EnumMap<HotelVisitPurpose, BigDecimal> value;

	public ClientNumberModifier(EnumMap<HotelVisitPurpose, BigDecimal> value) {
		this.value = value;
	}

	public EnumMap<HotelVisitPurpose, BigDecimal> getValue() {
		return value;
	}
}
