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

	private final Table innerTable = super.innerTable;

	public BaseFrame(String title) {
		super();
		innerTable.setFillParent(true);
		innerTable.add(titleLabel).growX().pad(20f).top().row();
		titleLabel.setText(title);
		titleLabel.setAlignment(Align.center);
		innerTable.add(mainTable).grow().pad(20f).row();
		this.setBackground("frame-glass-background");
		this.setResolutionChangeHandler(this::resize);
		this.resize();
	}

	private float getFrameWidth() {
		return (float) GraphicConfig.getResolution().WIDTH / 9 * 6;
	}

	private float getFrameHeight() {
		return (float) GraphicConfig.getResolution().HEIGHT / 9 * 6;
	}

	private void resize() {
		size(getFrameWidth(), getFrameHeight());
	}
}
