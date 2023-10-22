package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.config.GraphicConfig;

public abstract class BaseFrame extends WrapperTable {

	private final Label titleLabel = new Label("", GameSkin.getInstance(), FontType.H2.getWhiteVariantName());
	public final Table mainTable = new Table();

	public BaseFrame(String languagePath) {
		super(languagePath);
		this.align(Align.center);
		innerTable.add(titleLabel).growX().pad(BaseFrameStyle.getPadding()).top().row();
		titleLabel.setAlignment(Align.center);
		innerTable.add(mainTable).grow().pad(BaseFrameStyle.getPadding());
		this.setBackground("frame-glass-background");

		this.setLanguageChangeHandler(this::setTitle);
		this.clearActions();
		this.resetAnimationPosition();
	}

	private void setTitle(String text) {
		titleLabel.setText(text);
	}

	@Override
	public void validate() {}

	private static class BaseFrameStyle {
		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 20f;
			};
		}
	}
}
