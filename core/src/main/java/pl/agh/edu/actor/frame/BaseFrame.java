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
	private final float WIDTH = (float) GameConfig.RESOLUTION.getWidth() / 9 * 6;
	private final float HEIGHT = (float) GameConfig.RESOLUTION.getHeight() / 9 * 6;

	public BaseFrame() {
		super();
		Skin skin = HotelSkin.getInstance();
		NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("panel-glass-background"));
		add(new Image(background, Scaling.stretch, Align.center));
	}

	@Override
	public void layout() {
		super.layout();
		this.setBounds((float) GameConfig.RESOLUTION.getWidth() / 18 * 3, (float) GameConfig.RESOLUTION.getHeight() / 18 * 3, WIDTH, HEIGHT);
	}

}
