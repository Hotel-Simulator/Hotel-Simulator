package pl.agh.edu.engine.hotel.dificulty;

import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.MEDIUM;

import java.math.BigDecimal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.serialization.KryoConfig;

public class GameDifficultyManager {
	private static GameDifficultyManager instance;
	private final DifficultyLevel difficultyLevel;

	static {
		KryoConfig.kryo.register(GameDifficultyManager.class, new Serializer<GameDifficultyManager>() {
			@Override
			public void write(Kryo kryo, Output output, GameDifficultyManager object) {
				kryo.writeObject(output, object.difficultyLevel);
			}

			@Override
			public GameDifficultyManager read(Kryo kryo, Input input, Class<? extends GameDifficultyManager> type) {
				return new GameDifficultyManager(kryo.readObject(input, DifficultyLevel.class));
			}
		});
	}

	private GameDifficultyManager() {
		this.difficultyLevel = MEDIUM;
	}

	private GameDifficultyManager(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public static GameDifficultyManager getInstance() {
		if (instance == null) {
			instance = new GameDifficultyManager();
		}
		return instance;
	}

	public double getDifficultyMultiplier() {
		return JSONGameDataLoader.difficultyMultiplier.get(difficultyLevel);
	}

	public BigDecimal getInitialBalance() {
		return JSONGameDataLoader.initialBalance.get(difficultyLevel);
	}
}
