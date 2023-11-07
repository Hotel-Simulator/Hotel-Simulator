package pl.agh.edu.engine.client;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public abstract class ClientGroupModifierSupplier {

	public abstract EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier();

	protected EnumMap<HotelVisitPurpose, BigDecimal> getIdentity() {
		return Stream.of(HotelVisitPurpose.values())
				.collect(Collectors.toMap(
						e -> e,
						e -> ZERO,
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	protected BinaryOperator<EnumMap<HotelVisitPurpose, BigDecimal>> getAccumulator() {
		return (resultMap, enumMap) -> {
			enumMap.keySet().forEach(key -> {
				BigDecimal value = enumMap.get(key);
				resultMap.merge(key, value, BigDecimal::add);
			});
			return resultMap;
		};
	}
}
