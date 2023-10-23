package pl.agh.edu.model.opinion.bucket;

import java.math.BigDecimal;

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
}
