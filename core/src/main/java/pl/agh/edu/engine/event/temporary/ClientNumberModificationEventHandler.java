package pl.agh.edu.engine.event.temporary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public class ClientNumberModificationEventHandler {
	private static ClientNumberModificationEventHandler instance;
	private final List<ClientNumberModifier> modifiers = new ArrayList<>();

	private ClientNumberModificationEventHandler() {}

	public static ClientNumberModificationEventHandler getInstance() {
		if (instance == null)
			instance = new ClientNumberModificationEventHandler();
		return instance;
	}

	public void add(ClientNumberModifier modifier) {
		modifiers.add(modifier);
	}

	public void remove(ClientNumberModifier modifier) {
		modifiers.remove(modifier);
	}

	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return modifiers.stream()
				.map(ClientNumberModifier::getValue)
				.reduce(getIdentity(), getAccumulator());
	}

	private EnumMap<HotelVisitPurpose, BigDecimal> getIdentity() {
		return Stream.of(HotelVisitPurpose.values())
				.collect(Collectors.toMap(
						e -> e,
						e -> BigDecimal.ZERO,
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private BinaryOperator<EnumMap<HotelVisitPurpose, BigDecimal>> getAccumulator() {
		return (resultMap, enumMap) -> {
			enumMap.keySet().forEach(key -> {
				BigDecimal value = enumMap.get(key);
				resultMap.merge(key, value, BigDecimal::add);
			});
			return resultMap;
		};
	}
}
