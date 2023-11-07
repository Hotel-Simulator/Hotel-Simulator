package pl.agh.edu.engine.building_cost;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;

public class BuildingCostMultiplierHandler {
	private BigDecimal buildingCostMultiplier = ONE;
	private static BuildingCostMultiplierHandler instance;

	private BuildingCostMultiplierHandler() {}

	public static BuildingCostMultiplierHandler getInstance() {
		if (instance == null)
			instance = new BuildingCostMultiplierHandler();
		return instance;
	}

	public void modify(BigDecimal modifier) {
		buildingCostMultiplier = buildingCostMultiplier.multiply(ONE.add(modifier));
	}

	public BigDecimal getMultiplier() {
		return buildingCostMultiplier;
	}
}
