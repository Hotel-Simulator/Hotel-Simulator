package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;

public class DifficultyButton extends TextButton implements LanguageChangeListener, ResolutionChangeListener {
	public final DifficultyLevel difficulty;
	public final Skin skin;

	public DifficultyButton(DifficultyLevel difficulty, Skin skin) {
		super("", skin);
		this.difficulty = difficulty;
		this.skin = skin;
		this.setStyle(getStyle());

		setButtonText();
		listenForChanges();
	}

	private void listenForChanges() {
		ResolutionManager.addListener(this);
		LanguageManager.addListener(this);
	}

	public void setButtonText() {
		String path = "difficulty." + difficulty.toString().toLowerCase();
		setText(LanguageManager.get(path));
	}

	public TextButtonStyle getStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("difficulty-button-small", TextButtonStyle.class);
			case MEDIUM -> skin.get("difficulty-button-medium", TextButtonStyle.class);
			case LARGE -> skin.get("difficulty-button-large", TextButtonStyle.class);
		};
	}

	@Override
	public void onLanguageChange() {
		setButtonText();
	}

	@Override
	public void onResolutionChange() {
		setStyle(getStyle());
	}
}
