package pl.agh.edu.actor.TextActors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.WrapperTable;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.language.LanguageManager;

public class ValueTag extends WrapperTable {
	private final Skin skin = HotelSkin.getInstance();
	private Label tagLabel = new Label("", skin, "subtitle1");
	private Label valueLabel = new Label("", skin, "white-subtitle1");

	public ValueTag(String languagePath, String value) {
		super(languagePath);
		this.setBackground("value-tag-background");

		innerTable.add(tagLabel).grow();
		innerTable.add(valueLabel).grow();
		innerTable.pad(ValueTagStyle.getPadding());

		valueLabel.setText(value);
		valueLabel.setAlignment(Align.right, Align.right);

		tagLabel.setText(LanguageManager.get(languagePath));
		tagLabel.setAlignment(Align.left, Align.left);

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
