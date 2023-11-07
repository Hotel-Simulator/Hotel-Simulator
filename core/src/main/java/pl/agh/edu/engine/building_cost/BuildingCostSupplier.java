package pl.agh.edu.engine.building_cost;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.data.loader.JSONRoomDataLoader;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.utils.Pair;

public class BuildingCostSupplier {
	private static final BuildingCostMultiplierHandler multiplierHandler = BuildingCostMultiplierHandler.getInstance();

	public static BigDecimal roomBuildingCost(Pair<RoomRank, RoomSize> pair) {
		return JSONRoomDataLoader.roomBuildingCosts.get(pair)
				.multiply(multiplierHandler.getMultiplier()).setScale(0, RoundingMode.HALF_EVEN);
	}
}
