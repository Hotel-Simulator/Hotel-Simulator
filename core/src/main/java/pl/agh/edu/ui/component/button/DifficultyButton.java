package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;

public class DifficultyButton extends TextButton {
    public final DifficultyLevel difficulty;

    public DifficultyButton(String text, TextButtonStyle style, DifficultyLevel difficulty) {
        super(text, style);
        this.difficulty = difficulty;

//        if (scenariosSettings.getDifficultyButton().isPresent() && scenariosSettings.getDifficultyButton().get().difficulty == difficulty) {
//            setChecked(true);
//        }
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
}
