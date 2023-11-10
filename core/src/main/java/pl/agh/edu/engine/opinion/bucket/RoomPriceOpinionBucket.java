package pl.agh.edu.engine.opinion.bucket;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class RoomPriceOpinionBucket extends OpinionBucket {
	private final BigDecimal maxPrice;
	private BigDecimal offeredPrice;

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
