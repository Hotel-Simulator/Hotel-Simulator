package pl.agh.edu.engine.building_cost;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONAttractionDataLoader;
import pl.agh.edu.data.loader.JSONRoomDataLoader;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.Pair;

public class BuildingCostSupplier {
	private final BuildingCostMultiplierHandler multiplierHandler;

	static {
		KryoConfig.kryo.register(BuildingCostSupplier.class, new Serializer<BuildingCostSupplier>() {
			@Override
			public void write(Kryo kryo, Output output, BuildingCostSupplier object) {
				kryo.writeObject(output, object.multiplierHandler);
			}

			@Override
			public BuildingCostSupplier read(Kryo kryo, Input input, Class<? extends BuildingCostSupplier> type) {
				return new BuildingCostSupplier(kryo.readObject(input, BuildingCostMultiplierHandler.class));
			}
		});
	}

	public BuildingCostSupplier() {
		this.multiplierHandler = BuildingCostMultiplierHandler.getInstance();
	}

	private BuildingCostSupplier(BuildingCostMultiplierHandler multiplierHandler) {
		this.multiplierHandler = multiplierHandler;
	}

	public BigDecimal roomBuildingCost(Pair<RoomRank, RoomSize> pair) {
		return JSONRoomDataLoader.roomBuildingCosts.get(pair)
				.multiply(multiplierHandler.getMultiplier()).setScale(0, RoundingMode.HALF_EVEN);
	}

	public BigDecimal attractionBuildingCost(AttractionSize size) {
		return JSONAttractionDataLoader.buildCost.get(size)
				.multiply(multiplierHandler.getMultiplier()).setScale(0, RoundingMode.HALF_EVEN);
	}
}
