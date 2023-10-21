package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.GameSkin;

public class CustomLabel extends Label {
	public CustomLabel(String font, String backgroundPatch) {
		super("", GameSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = GameSkin.getInstance().getFont(font);
		labelStyle.background = new NinePatchDrawable(GameSkin.getInstance().getPatch(backgroundPatch));
		this.setStyle(labelStyle);
	}

	public CustomLabel(String font) {
		super("", GameSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = GameSkin.getInstance().getFont(font);
		this.setStyle(labelStyle);
	}
}
