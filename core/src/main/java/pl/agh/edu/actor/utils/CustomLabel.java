package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.HotelSkin;

public abstract class CustomLabel extends Label {
	public CustomLabel(String font, String backgroundPatch) {
		super("", HotelSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = HotelSkin.getInstance().getFont(font);
		labelStyle.background = new NinePatchDrawable(HotelSkin.getInstance().getPatch(backgroundPatch));
		this.setStyle(labelStyle);
	}

	public CustomLabel(String font) {
		super("", HotelSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = HotelSkin.getInstance().getFont(font);
		this.setStyle(labelStyle);
	}
}
