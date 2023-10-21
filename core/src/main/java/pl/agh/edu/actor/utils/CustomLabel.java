package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.HotelSkin;

public class CustomLabel extends Label {
	public CustomLabel(BitmapFont font, String backgroundPatch) {
		super("", HotelSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.background = new NinePatchDrawable(HotelSkin.getInstance().getPatch(backgroundPatch));
		this.setStyle(labelStyle);
	}

	public CustomLabel(BitmapFont font) {
		super("", HotelSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		this.setStyle(labelStyle);
	}
}
