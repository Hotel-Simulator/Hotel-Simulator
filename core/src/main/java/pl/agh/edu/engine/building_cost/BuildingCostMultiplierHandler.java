package pl.agh.edu.engine.building_cost;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class BuildingCostMultiplierHandler {
	private BigDecimal buildingCostMultiplier;

	public static void kryoRegister() {
		KryoConfig.kryo.register(BuildingCostMultiplierHandler.class, new Serializer<BuildingCostMultiplierHandler>() {
			@Override
			public void write(Kryo kryo, Output output, BuildingCostMultiplierHandler object) {
				kryo.writeObject(output, object.buildingCostMultiplier);
			}

			@Override
			public BuildingCostMultiplierHandler read(Kryo kryo, Input input, Class<? extends BuildingCostMultiplierHandler> type) {
				return new BuildingCostMultiplierHandler(kryo.readObject(input, BigDecimal.class));
			}
		});
	}

	public BuildingCostMultiplierHandler() {
		this.buildingCostMultiplier = ONE;
	}

	private BuildingCostMultiplierHandler(BigDecimal buildingCostMultiplier) {
		this.buildingCostMultiplier = buildingCostMultiplier;
	}

	public void modify(BigDecimal modifier) {
		buildingCostMultiplier = buildingCostMultiplier.multiply(ONE.add(modifier));
	}

	public BigDecimal getMultiplier() {
		return buildingCostMultiplier;
	}
}
