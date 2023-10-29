package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class DifficultyButton extends WrapperContainer<TextButton> implements LanguageChangeListener, ResolutionChangeListener {
	public final DifficultyLevel difficulty;
	public final Skin skin = GameSkin.getInstance();
	public final TextButton textButton;

	public DifficultyButton(DifficultyLevel difficulty) {
		this.difficulty = difficulty;
		textButton = new TextButton(null, getStyle());
		setActor(textButton);

		setButtonText();
		addSoundEvents();
		listenForChanges();
		setSize();
	}

	private void setSize() {
		switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> this.size(200f, 60f);
			case MEDIUM -> this.size(300f, 90f);
			case LARGE -> this.size(400f, 120f);
		}
	}

	private void addSoundEvents() {
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!textButton.isChecked()) {
					SoundAudio.CLICK_2.play();
					return true;
				}
				return false;
			}
		});
	}

	private void listenForChanges() {
		ResolutionManager.addListener(this);
		LanguageManager.addListener(this);
	}

	public void setButtonText() {
		String path = "difficulty." + difficulty.toString().toLowerCase();
		textButton.setText(LanguageManager.get(path));
	}

	public TextButton.TextButtonStyle getStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("difficulty-button-small", TextButton.TextButtonStyle.class);
			case MEDIUM -> skin.get("difficulty-button-medium", TextButton.TextButtonStyle.class);
			case LARGE -> skin.get("difficulty-button-large", TextButton.TextButtonStyle.class);
		};
	}

	public TextButton getTextButton() {
		return textButton;
	}

	@Override
	public void onLanguageChange() {
		setButtonText();
	}

	@Override
	public void onResolutionChange() {
		setSize();
		textButton.setStyle(getStyle());
	}
}
