package pl.agh.edu.engine.attraction;

import static pl.agh.edu.engine.attraction.AttractionState.ACTIVE;
import static pl.agh.edu.engine.attraction.AttractionState.BEING_BUILD;
import static pl.agh.edu.engine.attraction.AttractionState.SHUTTING_DOWN;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;
import pl.agh.edu.serialization.KryoConfig;

public class Attraction {
	public final AttractionType type;
	private AttractionSize size;
	private AttractionState state = BEING_BUILD;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Attraction.class, new Serializer<Attraction>() {
			@Override
			public void write(Kryo kryo, Output output, Attraction object) {
				kryo.writeObject(output, object.type);
				kryo.writeObject(output, object.size);
				kryo.writeObject(output, object.state);
			}

			@Override
			public Attraction read(Kryo kryo, Input input, Class<? extends Attraction> type) {
				Attraction attraction = new Attraction(
						kryo.readObject(input, AttractionType.class),
						kryo.readObject(input, AttractionSize.class));
				attraction.state = kryo.readObject(input, AttractionState.class);
				return attraction;
			}
		});
	}

	public Attraction(AttractionType type, AttractionSize size) {
		this.type = type;
		this.size = size;
	}

	public int getDailyCapacity() {
		return JSONAttractionDataLoader.dailyCapacity.get(size);
	}

	public BigDecimal getDailyExpenses() {
		return JSONAttractionDataLoader.dailyExpenses.get(size);
	}

	public void setSize(AttractionSize size) {
		this.size = size;
	}

	public AttractionSize getSize() {
		return size;
	}

	public AttractionState getState() {
		return state;
	}

	public void setState(AttractionState state) {
		this.state = state;
	}

	public boolean isWorking() {
		return state == ACTIVE || state == SHUTTING_DOWN;
	}
}
