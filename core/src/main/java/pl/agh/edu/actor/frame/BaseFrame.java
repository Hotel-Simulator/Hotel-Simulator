package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.config.GraphicConfig;

public abstract class BaseFrame extends Stack {
	public BaseFrame() {
		super();
		Skin skin = GameSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));
	}

	private float getFrameWidth() {
		return (float) GraphicConfig.getResolution().WIDTH / 9 * 6;
	}

	private float getFrameHeight() {
		return (float) GraphicConfig.getResolution().HEIGHT / 9 * 6;
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds(
				(GraphicConfig.getResolution().WIDTH - getFrameWidth()) / 2,
				(GraphicConfig.getResolution().HEIGHT - getFrameHeight()) / 2,
				getFrameWidth(),
				getFrameHeight());
	}
}
