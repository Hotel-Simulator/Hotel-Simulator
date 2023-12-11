package pl.agh.edu.ui.component.label;

import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.right;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE2;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class ValueTag extends WrapperTable {
	private final Actor valueLabel;
	private final CustomLabel tagLabel;
	private final Container<Image> separatorImageContainer = new Container<>(new Image(getGameSkin().getPatch("value-tag-separator")));

	private final Runnable updateHandler;

	public ValueTag(LanguageString tagLanguageString, String value) {
		super();

		tagLabel = new LanguageLabel(tagLanguageString, ValueTagStyle.getFont());
		CustomLabel valueLabel = new CustomLabel(ValueTagStyle.getFont());
		valueLabel.setText(value);
		this.valueLabel = valueLabel;

		updateHandler = () -> {
			tagLabel.setFont(ValueTagStyle.getFont());
			valueLabel.setFont(ValueTagStyle.getFont());
		};

		valueLabel.setAlignment(right, right);

		initLayout();
	}

	public ValueTag(LanguageString tagLanguageString, LanguageString valueLanguageString) {
		super();
		tagLabel = new LanguageLabel(tagLanguageString, ValueTagStyle.getFont());
		LanguageLabel valueLabel = new LanguageLabel(valueLanguageString, ValueTagStyle.getFont());
		this.valueLabel = valueLabel;

		updateHandler = () -> {
			tagLabel.setFont(ValueTagStyle.getFont());
			valueLabel.setFont(ValueTagStyle.getFont());
		};

		valueLabel.setAlignment(right, right);

		initLayout();
	}

	public ValueTag(LanguageString tagLanguageString, Actor component) {
		super();
		tagLabel = new LanguageLabel(tagLanguageString, SUBTITLE1.getName());
		valueLabel = component;

		updateHandler = () -> tagLabel.setFont(ValueTagStyle.getFont());

		initLayout();
	}

	protected void initLayout() {
		this.setBackground("value-tag-background");

		tagLabel.setAlignment(left, left);

		separatorImageContainer.fill();

		innerTable.add(tagLabel).growX().uniform();
		innerTable.add(separatorImageContainer).growY().width(2f);
		innerTable.add(valueLabel).growX().uniform();
		innerTable.setFillParent(true);

		this.size(ValueTagStyle.getWidth(), ValueTagStyle.getHeight());
		separatorImageContainer.height(ValueTagStyle.getHeight());
		innerTable.pad(ValueTagStyle.getPadding());
		updateHandler.run();
	}

	public void setValueColor(Color color) {
		valueLabel.setColor(color);
	}
	private static class ValueTagStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 55f;
				case MEDIUM -> 70f;
				case LARGE -> 90f;
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 500f;
				case MEDIUM -> 700f;
				case LARGE -> 1000f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 20f;
				case MEDIUM -> 30f;
				case LARGE -> 40f;
			};
		}

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SUBTITLE3.getName();
				case MEDIUM, LARGE -> SUBTITLE2.getName();
			};
		}
	}
}
