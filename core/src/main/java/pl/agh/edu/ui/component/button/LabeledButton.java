package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class LabeledButton extends WrapperContainer<TextButton> {
	private final Size type;
	private final TextButton button;

	public LabeledButton(Size type, LanguageString languageString) {
		super(languageString);
		this.type = type;
		this.button = new TextButton("", GameSkin.getInstance().get(type.toString(), TextButton.TextButtonStyle.class));
		this.button.setFillParent(true);
		this.button.getLabel().setWrap(true);

		this.setActor(button);

		this.setLanguageChangeHandler(this::updateLabel);
		this.setResolutionChangeHandler(this::setSizes);
		this.initChangeHandlers();
	}

	private void setSizes() {
		this.size(LabeledButtonStyle.getWidth(type), LabeledButtonStyle.getHeight(type));
	}

	private void updateLabel(String text) {
		button.setText(text);
	}

	private static class LabeledButtonStyle {

		public static float getRadius(Size type) {
			return switch (type) {
				case SMALL -> 20f;
				case MEDIUM -> 30f;
				case LARGE -> 40f;
			};
		}

		public static float getWidth(Size type) {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 50f + 4 * getRadius(type);
				case MEDIUM -> 100f + 4 * getRadius(type);
				case LARGE -> 200f + 4 * getRadius(type);
			};
		}

		public static float getHeight(Size type) {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f + 2 * getRadius(type);
				case MEDIUM -> 10f + 2 * getRadius(type);
				case LARGE -> 15f + 2 * getRadius(type);
			};
		}
	}
}
