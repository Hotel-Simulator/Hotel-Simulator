package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.GameConfig;
import pl.agh.edu.actor.HotelSkin;

public abstract class BaseFrame extends Stack {
	public BaseFrame() {
		super();
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("frame-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));
	}

	private float getFrameWidth() {
		return (float) GameConfig.RESOLUTION.WIDTH / 9 * 6;
	}

	private float getFrameHeight() {
		return (float) GameConfig.RESOLUTION.HEIGHT / 9 * 6;
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds(
				(GameConfig.RESOLUTION.WIDTH - getFrameWidth()) / 2,
				(GameConfig.RESOLUTION.HEIGHT - getFrameHeight()) / 2,
				getFrameWidth(),
				getFrameHeight());
	}
}
