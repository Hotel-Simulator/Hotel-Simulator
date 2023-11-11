package pl.agh.edu.ui.frame;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinFont.H2;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public abstract class BaseFrame extends WrapperTable {
	public final Table mainTable = new Table();
	public BaseFrame(LanguageString languageString) {
		super();
		this.align(center);
		Label titleLabel = new LanguageLabel(languageString, H2.getWhiteVariantName());
		innerTable.add(titleLabel).growX().pad(BaseFrameStyle.getPadding()).top().row();
		titleLabel.setAlignment(center);
		innerTable.add(mainTable).grow().pad(BaseFrameStyle.getPadding());
		this.setBackground("frame-glass-background");

		this.clearActions();
		this.resetAnimationPosition();
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
