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

	@Override
	public void layout() {
		super.layout();
		this.setBounds(
				(float) GameConfig.RESOLUTION.WIDTH / 18 * 3,
				(float) GameConfig.RESOLUTION.HEIGHT / 18 * 3,
				(float) GameConfig.RESOLUTION.WIDTH / 9 * 6,
				(float) GameConfig.RESOLUTION.HEIGHT / 9 * 6);
	}

}
