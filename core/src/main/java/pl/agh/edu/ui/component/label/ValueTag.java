package pl.agh.edu.ui.component.label;

import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.right;
import static pl.agh.edu.ui.utils.FontType.SUBTITLE1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class ValueTag extends WrapperTable {
	private final Skin skin = GameSkin.getInstance();
	private final Label valueLabel = new Label("", skin, SUBTITLE1.getWhiteVariantName());

	public ValueTag(String languagePath, String value) {
		super(languagePath);
		this.setBackground("value-tag-background");

		Label tagLabel = new Label("", skin, SUBTITLE1.getName());
		innerTable.add(tagLabel).grow();
		innerTable.add(valueLabel).grow();
		innerTable.pad(ValueTagStyle.getPadding());

		valueLabel.setText(value);
		valueLabel.setAlignment(right, right);

		tagLabel.setText(LanguageManager.get(languagePath));
		tagLabel.setAlignment(left, left);

		this.size(ValueTagStyle.getWidth(), ValueTagStyle.getHeight());
		this.setLanguageChangeHandler(tagLabel::setText);
		this.setResolutionChangeHandler(this::changeResolutionHandler);
	}

	public void setValueColor(Color color) {
		valueLabel.setColor(color);
	}

	private void changeResolutionHandler() {
		this.size(ValueTagStyle.getWidth(), ValueTagStyle.getHeight());
	}

	private static class ValueTagStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 40f;
				case MEDIUM -> 50f;
				case LARGE -> 60f;
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 400f;
				case MEDIUM -> 600f;
				case LARGE -> 900f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 20f;
				case MEDIUM -> 30f;
				case LARGE -> 40f;
			};
		}

	}
}
