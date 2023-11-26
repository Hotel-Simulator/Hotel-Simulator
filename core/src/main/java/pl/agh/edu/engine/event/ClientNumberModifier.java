package pl.agh.edu.engine.event;

import java.math.BigDecimal;
import java.util.EnumMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.EnumMapSerializer;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.serialization.KryoConfig;

public class ClientNumberModifier {
	private final EnumMap<HotelVisitPurpose, BigDecimal> value;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientNumberModifier.class, new Serializer<ClientNumberModifier>() {
			@Override
			public void write(Kryo kryo, Output output, ClientNumberModifier object) {
				kryo.writeObject(output, object.getValue(), new EnumMapSerializer(HotelVisitPurpose.class));
			}

			@Override
			public ClientNumberModifier read(Kryo kryo, Input input, Class<? extends ClientNumberModifier> type) {
				return new ClientNumberModifier(kryo.readObject(input, EnumMap.class, new EnumMapSerializer(HotelVisitPurpose.class)));
			}
		});
	}

	public ClientNumberModifier(EnumMap<HotelVisitPurpose, BigDecimal> value) {
		this.value = value;
	}

	public EnumMap<HotelVisitPurpose, BigDecimal> getValue() {
		return value;
	}
}
