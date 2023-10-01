package pl.agh.edu.actor.TextActors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class ValueTag extends Table implements LanguageChangeListener {
	private final Skin skin;
	private final String languagePath;
	private Label tagLabel;
	private Label valueLabel;

	public ValueTag(String languagePath, String value) {
		this.languagePath = languagePath;
		this.skin = HotelSkin.getInstance();
		Stack componentStack = new Stack();

		add(componentStack).height(ValueTagStyle.getHeight()).width(ValueTagStyle.getWidth());
		NinePatch background = skin.getPatch("valuetag-background");
		componentStack.add(new Image(background));
		componentStack.add(new ValueTagContent(languagePath, value));
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	private class ValueTagContent extends Table {
		ValueTagContent(String tag, String value) {

			this.padLeft(ValueTagStyle.getPadding()).padRight(ValueTagStyle.getPadding());

			tagLabel = new Label(tag, skin, "subtitle1_label");
			this.add(tagLabel).growX();

			valueLabel = new Label(value, skin, "white_subtitle1_label");
			valueLabel.setColor(skin.getColor("Gray_700"));
			valueLabel.setAlignment(Align.right);
			this.add(valueLabel).growX();
		}
	}

	public void setTagColor(Color color) {
		setColor(tagLabel, color);
	}

	public void setValueColor(Color color) {
		setColor(valueLabel, color);
	}

	private void setColor(Label coloredLabel, Color color) {
		Label.LabelStyle labelStyle = coloredLabel.getStyle();
		String labelName = labelStyle.font.toString();
		labelStyle.font = skin.getFont(labelName.startsWith("white-") ? labelName : "white-" + labelName);
		coloredLabel.setStyle(labelStyle);
		coloredLabel.setColor(color);
	}

	@Override
	public void onLanguageChange() {
		this.tagLabel.setText(LanguageManager.get(languagePath));
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
