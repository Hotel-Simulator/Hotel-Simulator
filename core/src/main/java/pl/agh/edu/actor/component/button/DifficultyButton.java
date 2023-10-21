package pl.agh.edu.actor.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.enums.DifficultyLevel;

public class DifficultyButton extends TextButton {
	public final DifficultyLevel difficulty;
	private boolean isSelected = false;

	public DifficultyButton(String text, TextButtonStyle style, DifficultyLevel difficulty) {
		super(text, style);
		this.difficulty = difficulty;
	}

	public void select() {
		isSelected = true;
	}

	public void unselect() {
		isSelected = false;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public DifficultyLevel getDifficulty() {
		return difficulty;
	}
}
