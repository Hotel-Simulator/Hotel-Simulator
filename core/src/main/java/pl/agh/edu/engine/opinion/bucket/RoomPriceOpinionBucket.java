package pl.agh.edu.engine.opinion.bucket;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class RoomPriceOpinionBucket extends OpinionBucket {
	private final BigDecimal maxPrice;
	private BigDecimal offeredPrice;

	static {
		KryoConfig.kryo.register(RoomPriceOpinionBucket.class, new Serializer<RoomPriceOpinionBucket>() {
			@Override
			public void write(Kryo kryo, Output output, RoomPriceOpinionBucket object) {
				kryo.writeObject(output, object.weight);
				kryo.writeObject(output, object.maxPrice);
				kryo.writeObjectOrNull(output, object.offeredPrice, BigDecimal.class);

			}

			@Override
			public RoomPriceOpinionBucket read(Kryo kryo, Input input, Class<? extends RoomPriceOpinionBucket> type) {
				RoomPriceOpinionBucket roomBreakingOpinionBucket = new RoomPriceOpinionBucket(
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, BigDecimal.class));

				roomBreakingOpinionBucket.setPrices(kryo.readObjectOrNull(input, BigDecimal.class));

				return roomBreakingOpinionBucket;
			}
		});
	}

	public RoomPriceOpinionBucket(int weight, BigDecimal maxPrice) {
		super(weight);
		this.maxPrice = maxPrice;
	}

	public void setPrices(BigDecimal offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	@Override
	public double getValue() {
		double priceRatio = offeredPrice.doubleValue() / maxPrice.doubleValue();
		return priceRatio >= 0.90 ? 0. : (priceRatio <= .8 ? 1. : (0.90 - priceRatio) * 10);
	}

	@Override
	public Optional<String> getComment() {
		double value = getValue();
		if (value == 1.) {
			return Optional.of("opinionComment.roomPrice.low");
		}
		if (value == 0.) {
			return Optional.of("opinionComment.roomPrice.high");

		}
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RoomPriceOpinionBucket that = (RoomPriceOpinionBucket) o;
		return Objects.equals(maxPrice, that.maxPrice) && Objects.equals(offeredPrice, that.offeredPrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maxPrice, offeredPrice);
	}
}
