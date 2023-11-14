package pl.agh.edu.engine.event.temporary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.client.ClientGroupModifierSupplier;
import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.serialization.KryoConfig;

public class ClientNumberModificationEventHandler extends ClientGroupModifierSupplier {
	private final List<ClientNumberModifier> modifiers;

	static {
		KryoConfig.kryo.register(ClientNumberModificationEventHandler.class, new Serializer<ClientNumberModificationEventHandler>() {
			@Override
			public void write(Kryo kryo, Output output, ClientNumberModificationEventHandler object) {
				kryo.writeObject(output, object.modifiers, KryoConfig.listSerializer(ClientNumberModifier.class));
			}

			@Override
			public ClientNumberModificationEventHandler read(Kryo kryo, Input input, Class<? extends ClientNumberModificationEventHandler> type) {
				return new ClientNumberModificationEventHandler(
						kryo.readObject(input, List.class, KryoConfig.listSerializer(ClientNumberModifier.class)));
			}
		});
	}

	public ClientNumberModificationEventHandler() {
		this.modifiers = new ArrayList<>();
	}

	private ClientNumberModificationEventHandler(List<ClientNumberModifier> modifiers) {
		this.modifiers = modifiers;
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
