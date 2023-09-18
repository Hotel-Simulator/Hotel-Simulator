package pl.agh.edu.management.game;

import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.utils.RandomUtils;

public class GameDifficultyManager {
    private double difficultyMultiplier;

    public GameDifficultyManager(){
        // Set user input here
        DifficultyLevel userChoice = DifficultyLevel.MEDIUM;
        setDifficulty(userChoice);

    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    private void setDifficulty(DifficultyLevel difficulty) {
        difficultyMultiplier = JSONHotelScenariosDataLoader.difficultyMultiplier.get(difficulty);
    }
}
