package pl.agh.edu.actor.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.actor.utils.ScenariosSettings;
import pl.agh.edu.enums.DifficultyLevel;

public class DifficultyButton extends TextButton {
	public final DifficultyLevel difficulty;

	public DifficultyButton(String text, TextButtonStyle style, DifficultyLevel difficulty, ScenariosSettings scenariosSettings) {
		super(text, style);
		this.difficulty = difficulty;

		if(scenariosSettings.getDifficultyButton().isPresent() && scenariosSettings.getDifficultyButton().get().difficulty == difficulty){
			setChecked(true);
		}
	}

	public DifficultyLevel getDifficulty() {
		return difficulty;
	}
}
