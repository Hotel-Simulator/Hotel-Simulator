package pl.agh.edu.ui.component.button;

import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class DifficultyButton extends WrapperContainer<TextButton> {
	public final DifficultyLevel difficulty;
	public final TextButton textButton;

	public DifficultyButton(DifficultyLevel difficulty) {
		super(getLanguageString(difficulty));
		this.difficulty = difficulty;
		textButton = new TextButton(null, getStyle());
		updateLanguageString();
		setActor(textButton);

		addSoundEvents();

		this.setLanguageChangeHandler(this::setButtonText);
		this.setResolutionChangeHandler(this::resize);
	}

	private void updateLanguageString() {
		this.updateLanguageString(getLanguageString(difficulty));
	}

	private static LanguageString getLanguageString(DifficultyLevel difficulty) {
		return new LanguageString("difficulty." + difficulty.toString().toLowerCase());
	}

	private void addSoundEvents() {
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!textButton.isChecked()) {
					CLICK.playSound();
					return true;
				}
				return false;
			}
		});
	}

	public void setButtonText(String text) {
		textButton.setText(text);
	}

	public void resize() {
		this.setSize(DifficultyButtonStyle.getWidth(), DifficultyButtonStyle.getHeight());
		textButton.setStyle(getStyle());
	}

	private TextButton.TextButtonStyle getStyle() {
		return skin.get(DifficultyButtonStyle.getStyleName(), TextButton.TextButtonStyle.class);
	}

	private static class DifficultyButtonStyle {
		private static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 200f;
				case MEDIUM -> 300f;
				case LARGE -> 400f;
			};
		}

		private static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 60f;
				case MEDIUM -> 90f;
				case LARGE -> 120f;
			};
		}

		private static String getStyleName() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> "difficulty-button-small";
				case MEDIUM -> "difficulty-button-medium";
				case LARGE -> "difficulty-button-large";
			};
		}
	}
}
