package pl.agh.edu.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;

public class GameSaveHandler {
	private static GameSaveHandler instance;
	private static String currentGameSaveName;
	private final File saveFolder;
	private final OperatingSystem os;

	private GameSaveHandler() {
		this.os = OperatingSystem.detect();
		String saveFolderPath = System.getProperty("user.home") + os.getHomeDirectoryPath() + os.pathSeparator + "HotelSimulator" + os.pathSeparator + "saves";
		saveFolder = new File(saveFolderPath);

		if (!saveFolder.exists()) {
			saveFolder.mkdirs();
		}
	}

	public static GameSaveHandler getInstance() {
		if (instance == null)
			instance = new GameSaveHandler();
		return instance;
	}

	public boolean gameSaveWithThisNameAlreadyExist(String fileName) {
		return getGameSavesNames().contains(fileName);
	}

	public Set<String> getGameSavesNames() {

		return Optional.ofNullable(saveFolder.listFiles())
				.stream()
				.flatMap(Arrays::stream)
				.map(File::getName)
				.map(name -> name.replaceAll("\\.bin$", ""))
				.collect(Collectors.toSet());
	}

	public String getCurrentGameSavePath() {
		return saveFolder.getAbsolutePath() + os.pathSeparator + currentGameSaveName + ".bin";
	}

	public Engine startNewGame(String hotelName, HotelType hotelType, DifficultyLevel difficultyLevel) {
		currentGameSaveName = hotelName;
		return new Engine(hotelName, hotelType, difficultyLevel);
	}

	public Engine loadSavedGame(String gameSaveName) {
		currentGameSaveName = gameSaveName;
		Engine engine;
		try {
			Input input = new Input(new FileInputStream(getCurrentGameSavePath()));
			engine = KryoConfig.kryo.readObject(input, Engine.class);
			input.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return engine;
	}

	public void saveGame(Engine engine) {
		try {
			Output output = new Output(new FileOutputStream(getCurrentGameSavePath()));
			KryoConfig.kryo.writeObject(output, engine);
			output.close();

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
