package pl.agh.edu.engine.event.temporary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import pl.agh.edu.engine.client.ClientGroupModifierSupplier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;

public class ClientNumberModificationEventHandler extends ClientGroupModifierSupplier {
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

	@Override
	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return modifiers.stream()
				.map(ClientNumberModifier::getValue)
				.reduce(getIdentity(), getAccumulator());
	}
}
