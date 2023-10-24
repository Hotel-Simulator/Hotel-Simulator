package pl.agh.edu.engine.opinion.bucket;

import java.math.BigDecimal;

public class RoomPriceOpinionBucket extends OpinionBucket {
	private BigDecimal maxPrice;
	private BigDecimal offeredPrice;

	public RoomPriceOpinionBucket(int weight) {
		super(weight);
	}

	public void setPrices(BigDecimal maxPrice, BigDecimal offeredPrice) {
		this.maxPrice = maxPrice;
		this.offeredPrice = offeredPrice;
	}

	@Override
	public double getValue() {
		double priceRatio = offeredPrice.doubleValue() / maxPrice.doubleValue();
		return priceRatio >= 0.90 ? 0. : (priceRatio <= .8 ? 1. : (0.90 - priceRatio) * 10);
	}
}
