package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioLabeledButton extends WrapperContainer<TextButton> {
	private final TextButton button;

	public ScenarioLabeledButton(LanguageString languageString) {
		super(languageString);
		this.button = new TextButton("", getButtonStyle());
		this.button.setFillParent(true);
		this.button.getLabel().setWrap(true);

		this.setActor(button);

		this.setLanguageChangeHandler(this::updateLabel);
		this.setResolutionChangeHandler(this::updateSize);
		this.initChangeHandlers();
	}

	private void updateSize() {
		this.button.setStyle(getButtonStyle());
		this.size(ScenarioLabeledButtonStyle.getWidth(), ScenarioLabeledButtonStyle.getHeight());
	}

	private TextButton.TextButtonStyle getButtonStyle() {
		return getGameSkin().get(ScenarioLabeledButtonStyle.getButtonStyleName(), TextButton.TextButtonStyle.class);
	}

	private void updateLabel(String text) {
		button.setText(text);
	}

	private static class ScenarioLabeledButtonStyle {
		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 300f;
				case MEDIUM -> 400f;
				case LARGE -> 500f;
			};
		}

		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 100f;
				case MEDIUM -> 130f;
				case LARGE -> 180f;
			};
		}

		public static String getButtonStyleName() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> "difficulty-play-back-small";
				case MEDIUM -> "difficulty-play-back-medium";
				case LARGE -> "difficulty-play-back-large";
			};
		}
	}
}
